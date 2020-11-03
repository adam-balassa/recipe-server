package hu.balassa.recipe.controller

import hu.balassa.recipe.dto.NewStreetKitchenRecipe
import hu.balassa.recipe.dto.RecipeDto
import hu.balassa.recipe.dto.RecipeHeader
import hu.balassa.recipe.service.RecipeService
import hu.balassa.recipe.service.mapping.DtoMapper
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/recipe")
class RecipeController (
        private val service: RecipeService
) {
    @GetMapping
    fun listRecipes(): List<RecipeHeader> = service.getAllRecipes().map {
        DtoMapper.recipeToHeaderDto(it)
    }

    @GetMapping("/{id}")
    fun getRecipe(@PathVariable("id") id: Long): RecipeDto = service.getRecipe(id).let {
        DtoMapper.recipeToDto(it)
    }

    @PostMapping
    fun addRecipe(@RequestBody recipe: RecipeDto) = DtoMapper.recipeToDto(
            service.saveRecipe(DtoMapper.recipeToModel(recipe).apply { id = null })
    )

    @PutMapping("/{id}")
    fun updateRecipe(@RequestBody recipe: RecipeDto, @PathVariable("id") id: Long) = DtoMapper.recipeToDto(
            service.saveRecipe(DtoMapper.recipeToModel(recipe).also { it.id = id })
    )

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteRecipe(@PathVariable("id") id: Long) = service.deleteRecipe(id)


    @PostMapping("/streetkitchen")
    fun addStreetKitchenRecipe(@RequestBody info: NewStreetKitchenRecipe): RecipeDto {
        val recipe = service.addStreetKitchenRecipe(info)
        return DtoMapper.recipeToDto(recipe)
    }
}