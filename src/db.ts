import { DynamoDB } from 'aws-sdk';
import Config from './config';
import { Recipe, RecipeBase } from './model';

const db = new DynamoDB.DocumentClient();

export const listRecipes = (): Promise<RecipeBase[]> => db.scan({
  TableName: Config.dynamoDbTableName,
  ProjectionExpression: 'id, #n, imageUrl, quantity, quantity2, vegetarian, category',
  ExpressionAttributeNames: { '#n': 'name' }
}).promise().then(({ Items }) => Items as RecipeBase[]);

export const getAllRecipes = (): Promise<Recipe[]> => db.scan({
  TableName: Config.dynamoDbTableName
}).promise().then(({ Items }) => Items as Recipe[]);

export const getRecipe = (id: string) => db.get({
  TableName: Config.dynamoDbTableName,
  Key: { id }
}).promise().then(({ Item }) => Item as Recipe);
