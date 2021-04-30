package hu.balassa.recipe.service.mapping

import hu.balassa.recipe.dto.RecipeDto
import hu.balassa.recipe.dto.RecipeHeader
import hu.balassa.recipe.model.Recipe
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper
interface DtoModelMapper {
    @Mapping(target = "isVegetarian", source = "vegetarian")
    fun recipeModelToHeaderDto(recipe: Recipe): RecipeHeader
    fun recipeModelsToHeaderDtos(recipe: List<Recipe>): List<RecipeHeader>
    @Mapping(target = "isVegetarian", source = "vegetarian")
    fun recipeModelToDto(recipe: Recipe): RecipeDto
    fun recipeDtoToModel(recipe: RecipeDto): Recipe
}