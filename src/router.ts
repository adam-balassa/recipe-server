import { APIGatewayProxyEvent } from 'aws-lambda';
import { lambda } from './lambda';
import * as db from './db';
import { Recipe, RecipeDto, RecipeDtoBase } from './model';
import { modelBaseToDto, modelToDto } from './mapper';

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
