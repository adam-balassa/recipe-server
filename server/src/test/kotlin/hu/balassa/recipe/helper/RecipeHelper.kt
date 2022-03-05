package hu.balassa.recipe.helper

import com.fasterxml.jackson.databind.ObjectMapper
import hu.balassa.recipe.model.Category
import hu.balassa.recipe.model.IngredientGroup
import hu.balassa.recipe.model.Recipe
import org.springframework.util.ResourceUtils

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
            it.vegetarian = isVegetarian
        }
    }

    fun loadRecipe(fileName: String): Recipe {
        val file = ResourceUtils.getFile("classpath:recipes/$fileName.json")
        return ObjectMapper().readValue(file, Recipe::class.java)
    }
}