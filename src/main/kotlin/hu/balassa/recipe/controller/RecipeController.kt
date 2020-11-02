package hu.balassa.recipe.controller

import hu.balassa.recipe.dto.NewStreetKitchenRecipe
import hu.balassa.recipe.dto.RecipeDto
import hu.balassa.recipe.service.RecipeService
import hu.balassa.recipe.service.mapping.DtoMapper
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/recipe")
class RecipeController (
        private val service: RecipeService
) {
    @GetMapping
    fun listRecipes() = service.getAllRecipes().map {
        DtoMapper.recipeToDto(it)
    }

    @PostMapping
    fun addRecipe(@RequestBody recipe: RecipeDto) = DtoMapper.recipeToDto(
            service.addRecipe(DtoMapper.recipeToModel(recipe))
    )

    @PostMapping("/streetkitchen")
    fun addStreetKitchenRecipe(@RequestBody info: NewStreetKitchenRecipe): RecipeDto {
        val recipe = service.addStreetKitchenRecipe(info)
        return DtoMapper.recipeToDto(recipe)
    }
}