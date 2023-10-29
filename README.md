<!--
title: 'AWS Simple HTTP Endpoint example in NodeJS with Typescript'
description: 'This template demonstrates how to make a simple HTTP API with Node.js and Typescript running on AWS Lambda and API Gateway using the Serverless Framework v3.'
layout: Doc
framework: v3
platform: AWS
language: nodeJS
authorLink: 'https://github.com/serverless'
authorName: 'Serverless, inc.'
authorAvatar: 'https://avatars1.githubusercontent.com/u/13742415?s=200&v=4'
-->

# Serverless Framework Node with Typescript HTTP API on AWS

This template demonstrates how to make a simple HTTP API with Node.js and Typescript running on AWS Lambda and API Gateway using the Serverless Framework v3.

This template does not include any kind of persistence (database). For more advanced examples, check out the [serverless/examples repository](https://github.com/serverless/examples) which includes Typescript, Mongo, DynamoDB and other examples.

## Setup

### Install dependencies
Run this command to initialize a new project in a new working directory.

```shell
npm install
```
### Configure AWS
Set the credentials in `~/.aws/credentials`, including a profile, called `serverless`


## Usage

### Deploy

```shell
serverless deploy
```

### Invoke the function locally.
**getRecipeById**
```shell
serverless invoke local --function getRecipeById -d '{ "pathParameters": { "id": "ee2d067d-50ee-4b99-8ccb-1442488c7260" } }'
```
**addRecipe**
```shell
serverless invoke local --function addRecipe --path data/recipe.json
```


