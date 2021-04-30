package hu.balassa.recipe.helpers

import hu.balassa.recipe.model.Category
import hu.balassa.recipe.model.IngredientGroup
import hu.balassa.recipe.model.Recipe
import org.assertj.core.api.Assertions

object RecipeHelper {
    fun recipeOf(
        id: String? = null,
        name: String = "name",
        imageUrl: String? = "https://amazonaws.com/myimage",
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

    fun recipeOf(recipe: Map<String, String>): Recipe = recipeOf(
        id = recipe["id"],
        name = recipe["name"]!!,
        category = Category.valueOf(recipe["category"]!!),
        imageUrl = recipe["imageUrl"],
        quantity = recipe["quantity"]!!.toInt(),
        quantity2 = recipe["quantity2"]?.toInt(),
        isVegetarian = recipe["isVegetarian"]?.toBoolean() ?: false,
        instructions = recipe["instructions"]!!.split(","),
        ingredientGroups = emptyList()
    )

    fun verifyRecipe(actualRecipe: Recipe, expectedRecipe: Recipe) {
        if (!expectedRecipe.id.isNullOrBlank()) Assertions.assertThat(actualRecipe.id).isEqualTo(expectedRecipe.id)
        Assertions.assertThat(actualRecipe.name).isEqualTo(expectedRecipe.name)
        Assertions.assertThat(actualRecipe.category).isEqualTo(expectedRecipe.category)
        Assertions.assertThat(actualRecipe.imageUrl).isEqualTo(expectedRecipe.imageUrl)
        Assertions.assertThat(actualRecipe.isVegetarian).isEqualTo(expectedRecipe.isVegetarian)
        Assertions.assertThat(actualRecipe.quantity).isEqualTo(expectedRecipe.quantity)
        Assertions.assertThat(actualRecipe.quantity2).isEqualTo(expectedRecipe.quantity2)
        Assertions.assertThat(actualRecipe.instructions).containsExactlyElementsOf(expectedRecipe.instructions)
        Assertions.assertThat(actualRecipe.ingredientGroups).hasSize(0)
    }
}