export type Category = 'MAIN' | 'DESSERT' | 'BREAKFAST' | 'OTHER';

export interface RecipeBase {
  id: string;
  name: string;
  imageUrl?: string;
  quantity: number;
  quantity2?: number;
  vegetarian: boolean;
  category: Category;
}

export interface Recipe extends RecipeBase {
  instructions: string[];
  ingredientGroups: {
    name?: string;
    ingredients: Ingredient[];
  }[];
}

export interface Ingredient {
  name: string;
  quantity?: number;
  quantity2?: number;
}

export interface RecipeDtoBase {
  id: string;
  name: string;
  imageUrl: string;
  quantity: number;
  quantity2?: number;
  isVegetarian: boolean;
  category: Category;
}

export interface RecipeDto extends RecipeDtoBase {
  instructions: string[];
  ingredientGroups: {
    name?: string;
    ingredients: Ingredient[];
  }[];
}