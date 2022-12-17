import { APIGatewayProxyEvent, APIGatewayProxyResult } from "aws-lambda";
import { DynamoDB } from 'aws-sdk'

export const hello = async (
  event: APIGatewayProxyEvent
): Promise<APIGatewayProxyResult> => {
  const db = new DynamoDB.DocumentClient();
  const recipes = await db.scan({
    TableName: process.env.DYNAMODB_TABLE,
  }).promise();
  return {
    statusCode: 200,
    body: JSON.stringify(
      {
        message: "Go Serverless v3.0! Your function executed successfully!",
        recipes: recipes.Items
      },
      null,
      2
    ),
  };
};
