import { DynamoDB } from 'aws-sdk'
import Config from './config';

const db = new DynamoDB.DocumentClient();

export const listRecipes = () => db.scan({
  TableName: Config.dynamoDbTableName,
  ProjectionExpression: "id, #n, imageUrl, quantity, quantity2, vegetarian, category",
  ExpressionAttributeNames: { '#n': 'name' }
}).promise().then(({ Items }) => Items);

export const getRecipe = (id: string) => db.get({
  TableName: Config.dynamoDbTableName,
  Key: { id }
}).promise().then(({ Item }) => Item);