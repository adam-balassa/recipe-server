package hu.balassa.recipe.service.mapping

import hu.balassa.recipe.dto.IngredientDto
import hu.balassa.recipe.dto.IngredientGroupDto
import hu.balassa.recipe.dto.RecipeDto
import hu.balassa.recipe.model.Ingredient
import hu.balassa.recipe.model.IngredientGroup
import hu.balassa.recipe.model.Recipe
import org.mapstruct.Mapper

@Mapper
class DtoMapper {
    companion object {
        fun recipeToDto(recipe: Recipe): RecipeDto {
            fun ingredientToDto(ingredient: Ingredient) = IngredientDto().apply {
                quantity = ingredient.quantity
                quantity2 = ingredient.quantity2
                name = ingredient.name
            }

            fun ingredientGroupToDto(ingredientGroup: IngredientGroup) = IngredientGroupDto().apply {
                name = ingredientGroup.name
                ingredients = ingredientGroup.ingredients.map { ingredient -> ingredientToDto(ingredient) }
            }

            return RecipeDto().apply {
                name = recipe.name
                imageUrl = recipe.imageUrl
                quantity = recipe.quantity
                quantity2 = recipe.quantity2
                ingredientGroups = recipe.ingredientGroups.map { ingredientGroup -> ingredientGroupToDto(ingredientGroup) }
                instructions = recipe.instructions
            }
        }

        fun recipeToModel(recipe: RecipeDto): Recipe {
            fun ingredientToModel(ingredient: IngredientDto) = Ingredient().apply {
                quantity = ingredient.quantity
                quantity2 = ingredient.quantity2
                name = ingredient.name
            }

            fun ingredientGroupToModel(ingredientGroup: IngredientGroupDto) = IngredientGroup().apply {
                name = ingredientGroup.name
                ingredients = ingredientGroup.ingredients.map { ingredient -> ingredientToModel(ingredient) }.toSet()
            }

            return Recipe().apply {
                name = recipe.name
                imageUrl = recipe.imageUrl
                quantity = recipe.quantity
                quantity2 = recipe.quantity2
                ingredientGroups = recipe.ingredientGroups.map { ingredientGroup -> ingredientGroupToModel(ingredientGroup) }.toSet()
                instructions = recipe.instructions
            }
        }
    }

}