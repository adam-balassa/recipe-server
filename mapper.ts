import { Recipe, RecipeBase, RecipeDto, RecipeDtoBase } from './model';

export const modelBaseToDto = (model: RecipeBase): RecipeDtoBase => ({
  id: model.id,
  name: model.name,
  imageUrl: model.imageUrl ?? '',
  quantity: model.quantity,
  quantity2: model.quantity2,
  isVegetarian: model.vegetarian,
  category: model.category,
});

export const modelToDto = (model: Recipe): RecipeDto => ({
  ...modelBaseToDto(model),
  instructions: model.instructions,
  ingredientGroups: model.ingredientGroups
});
