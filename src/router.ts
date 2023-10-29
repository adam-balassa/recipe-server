import { APIGatewayProxyEvent } from 'aws-lambda';
import { lambda } from './lambda';
import * as db from './db';
import { Recipe, RecipeDto, RecipeDtoBase } from './model';
import { dtoToModel, modelBaseToDto, modelToDto } from './mapper';
import { AppError } from "./error";
import { generateId, uploadImage } from "./utils";
import { deleteRecipe } from "./db";

export const listRecipes = (event: APIGatewayProxyEvent) => lambda<RecipeDtoBase[]>(event, async () => {
  return db.listRecipes().then(recipes => recipes.map(modelBaseToDto));
});

export const getRecipeById = (event: APIGatewayProxyEvent) => lambda<RecipeDto>(event, async ({ pathParams }) => {
  return modelToDto(await db.getRecipe(pathParams?.id ?? ''));
});

const searchRecipes = async (keywords: string[]): Promise<Recipe[]> => {
  const calculatePriority = (recipe: Recipe) => {
    let priority = 0;
    keywords.forEach(keyword => {
      if (recipe.name.includes(keyword)) priority += 10;
      if (recipe.ingredientGroups
        .flatMap(({ ingredients }) => ingredients)
        .some(({ name }) => name.includes(keyword))) priority += 2;
      if (recipe.instructions.some(instruction => instruction.includes(keyword))) priority += 2;
    });
    return priority;
  };

  const recipes = await db.getAllRecipes();
  return recipes
  .flatMap(recipe => {
    const priority = calculatePriority(recipe);
    if (!priority) return [];
    return [{ recipe, priority }];
  })
  .sort((a, b) => b.priority - a.priority)
  .map(({ recipe }) => recipe);
};

export const searchRecipe = (event: APIGatewayProxyEvent) => lambda<RecipeDtoBase[]>(event, async ({ queryParams }) => {
  const keywords = queryParams.keywords?.split(',') ?? [];
  return searchRecipes(keywords).then(recipes => recipes.map(modelBaseToDto));
});

export const similarRecipes = (event: APIGatewayProxyEvent) => lambda<RecipeDtoBase[]>(event, async ({ pathParams }) => {
  const recipe = await db.getRecipe(pathParams?.id ?? '');
  const keywords = recipe.ingredientGroups
    .flatMap(({ ingredients }) => ingredients)
    .map(({ name }) => name.split(' ').pop() as string);

  return searchRecipes(keywords).then(recipes =>
    recipes
      .filter(({ id }) => id !== pathParams?.id)
      .slice(0, 6)
      .map(modelBaseToDto));
});

export const addRecipe = (event: APIGatewayProxyEvent) => lambda<RecipeDto, RecipeDto>(
  event,
  async ({ pathParams, body }) => {
    if (!body) throw new AppError({ message: 'No request body', status: 400 })
    if (body.imageUrl && !body.imageUrl.includes('amazonaws.com')) {
      body.imageUrl = await uploadImage(body.imageUrl)
    }
    const recipe = dtoToModel({ ...body, id: pathParams?.id ?? generateId() })
    await db.saveRecipe(recipe)
    return modelToDto(recipe);
  }
)

export const deleteRecipeById = (event: APIGatewayProxyEvent) => lambda(event, async ({ pathParams }) => {
  await db.deleteRecipe(pathParams?.id ?? '');
});
