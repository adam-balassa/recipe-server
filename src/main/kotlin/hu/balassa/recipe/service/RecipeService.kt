package hu.balassa.recipe.service

import hu.balassa.recipe.model.Recipe
import hu.balassa.recipe.repository.RecipeRepository
import org.springframework.stereotype.Service

@Service
class RecipeService (
        private val repository: RecipeRepository
) {
    fun getAllRecipes(): List<Recipe> = repository.findAll()
}