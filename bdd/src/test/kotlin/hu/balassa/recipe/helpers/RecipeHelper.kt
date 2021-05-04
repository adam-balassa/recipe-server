package hu.balassa.recipe.helpers

import hu.balassa.recipe.dto.IngredientGroupDto
import hu.balassa.recipe.dto.RecipeDto
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
        ingredientGroups: List<IngredientGroupDto> = listOf(),
        instructions: List<String> = listOf("instruction"),
        category: Category = Category.MAIN,
        isVegetarian: Boolean = false
    ): RecipeDto {
        return RecipeDto(
            id, name, imageUrl, quantity, quantity2, isVegetarian,category, ingredientGroups, instructions
        )
    }

    fun recipeOf(recipe: Map<String, String>): RecipeDto = RecipeDto(
        id = recipe["id"],
        name = recipe["name"]!!,
        category = Category.valueOf(recipe["category"]!!),
        imageUrl = recipe["imageUrl"],
        quantity = recipe["quantity"]!!.toInt(),
        quantity2 = recipe["quantity2"]?.toInt(),
        vegetarian = recipe["isVegetarian"]?.toBoolean() ?: false,
        instructions = recipe["instructions"]!!.split(","),
        ingredientGroups = emptyList()
    )

    fun verifyRecipe(actualRecipe: RecipeDto, expectedRecipe: RecipeDto) {
        if (!expectedRecipe.id.isNullOrBlank()) Assertions.assertThat(actualRecipe.id).isEqualTo(expectedRecipe.id)
        Assertions.assertThat(actualRecipe.name).isEqualTo(expectedRecipe.name)
        Assertions.assertThat(actualRecipe.category).isEqualTo(expectedRecipe.category)
        Assertions.assertThat(actualRecipe.imageUrl).isEqualTo(expectedRecipe.imageUrl)
        Assertions.assertThat(actualRecipe.vegetarian).isEqualTo(expectedRecipe.vegetarian)
        Assertions.assertThat(actualRecipe.quantity).isEqualTo(expectedRecipe.quantity)
        Assertions.assertThat(actualRecipe.quantity2).isEqualTo(expectedRecipe.quantity2)
        Assertions.assertThat(actualRecipe.instructions).containsExactlyElementsOf(expectedRecipe.instructions)
        Assertions.assertThat(actualRecipe.ingredientGroups).hasSize(0)
    }
}