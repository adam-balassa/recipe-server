package hu.balassa.recipe.service

import hu.balassa.recipe.dto.NewStreetKitchenRecipe
import hu.balassa.recipe.model.Recipe
import hu.balassa.recipe.repository.RecipeRepository
import org.springframework.stereotype.Service

@Service
class RecipeService (
        private val repository: RecipeRepository,
        private val streetKitchenService: StreetKitchenService
) {
    fun getAllRecipes(): List<Recipe> = repository.findAll()

    fun saveRecipe(recipe: Recipe): Recipe = repository.save(recipe)

    fun addStreetKitchenRecipe(info: NewStreetKitchenRecipe): Recipe {
        val recipe = streetKitchenService.getRecipe(info.url)
        return repository.save(recipe)
    }

    fun deleteRecipe(id: Long) = repository.deleteById(id)

    fun getRecipe(id: Long): Recipe = repository.findWithDetails(id)
}