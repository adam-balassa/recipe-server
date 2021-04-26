package hu.balassa.recipe.helpers

import hu.balassa.recipe.model.Category
import hu.balassa.recipe.model.IngredientGroup
import hu.balassa.recipe.model.Recipe

object RecipeHelper {
    fun recipeOf(
        id: String? = null,
        name: String = "name",
        imageUrl: String? = "imageUrl",
        quantity: Int = 1,
        quantity2: Int? = null,
        ingredientGroups: List<IngredientGroup> = listOf(),
        instructions: List<String> = listOf("instruction"),
        category: Category = Category.MAIN,
        isVegetarian: Boolean = false
    ): Recipe {
        return Recipe().also {
            it.id = id
            it.name = name
            it.imageUrl = imageUrl
            it.quantity = quantity
            it.quantity2 = quantity2
            it.ingredientGroups = ingredientGroups
            it.instructions = instructions
            it.category = category
            it.isVegetarian = isVegetarian
        }
    }
}