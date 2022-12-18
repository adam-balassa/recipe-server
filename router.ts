import { APIGatewayProxyEvent } from "aws-lambda";
import { lambda } from './lambda';
import * as db from './db';

export const listRecipes = (event: APIGatewayProxyEvent) => lambda(event, async () => {
  return (await db.listRecipes()).map(recipe => ({
    ...recipe,
    isVegetarian: recipe.vegetarian
  }));
});

export const getRecipeById = (event: APIGatewayProxyEvent) => lambda(event, async ({ pathParams }) => {
  const recipe = await db.getRecipe(pathParams?.id);
  return { ...recipe, isVegetarian: recipe.vegetarian };
});