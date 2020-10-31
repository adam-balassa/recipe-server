package hu.balassa.recipe.controller

import hu.balassa.recipe.service.RecipeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/recipe")
class RecipeController (
        private val service: RecipeService
) {
    @GetMapping
    fun listRecipes() = service.getAllRecipes()
}