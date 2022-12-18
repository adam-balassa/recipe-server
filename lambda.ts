import { APIGatewayProxyEvent, APIGatewayProxyResult } from "aws-lambda";

export const lambda = async <B>(
  event: APIGatewayProxyEvent, 
  callback: (request: {pathParams?: {[key: string]: string}, body?: B}) => Promise<any>
): Promise<APIGatewayProxyResult> => {
  const body = event.body && JSON.parse(event.body);
  const pathParams = (event.pathParameters && (event.pathParameters as {[key: string]: string})) ?? undefined;
  const result = await callback({ pathParams, body })
  return {
    statusCode: 200,
    body: JSON.stringify(result, null, 2),
  };
}