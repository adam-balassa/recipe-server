import { APIGatewayProxyEvent, APIGatewayProxyResult } from 'aws-lambda';
import { AppError } from "./error";

export const lambda = async <T, B = null>(
  event: APIGatewayProxyEvent,
  callback: (request: {
    pathParams: {[key: string]: string},
    queryParams: {[key: string]: string},
    body?: B
  }) => Promise<T>
): Promise<APIGatewayProxyResult> => {
  try {
    const body = event.body && JSON.parse(event.body);
    const pathParams = (event.pathParameters && (event.pathParameters as {[key: string]: string})) ?? {};
    const queryParams = (event.queryStringParameters && (event.queryStringParameters as {[key: string]: string})) ?? {};
    const result = await callback({ pathParams, queryParams, body });
    return {
      statusCode: 200,
      body: JSON.stringify(result, null, 2),
    };
  } catch (e) {
    console.error(e)
    if (e instanceof AppError) {
      return {
        statusCode: e.status,
        body: JSON.stringify({ message: e.message }),
      }
    }
    return {
      statusCode: 500,
      body: JSON.stringify({ message: 'An unexpected error occurred' }),
    };
  }
}
