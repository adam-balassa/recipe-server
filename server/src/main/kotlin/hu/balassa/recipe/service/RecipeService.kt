package hu.balassa.recipe.service

import hu.balassa.recipe.dto.NewStreetKitchenRecipe
import hu.balassa.recipe.exception.NotFoundException
import hu.balassa.recipe.model.Ingredient
import hu.balassa.recipe.model.Recipe
import hu.balassa.recipe.repository.RecipeRepository
import org.springframework.stereotype.Service

@Service
class RecipeService(
    private val repository: RecipeRepository,
    private val imageUploadClient: ImageUploadClient,
    private val streetKitchenService: StreetKitchenService
) {

    fun getAllRecipes(): List<Recipe> = repository.findAll().toList()

    fun saveRecipe(recipe: Recipe): Recipe {
        if (recipe.imageUrl?.contains("amazonaws.com") != true) {
            recipe.imageUrl = imageUploadClient.uploadImageFromImageURL(recipe.imageUrl!!)
        }
        return repository.save(recipe)
    }

    fun addStreetKitchenRecipe(info: NewStreetKitchenRecipe): Recipe {
        val recipe = streetKitchenService.getRecipe(info.url)
        recipe.imageUrl = recipe.imageUrl?.let {
            imageUploadClient.uploadImageFromImageURL(it)
        }
        return repository.save(recipe)
    }

    fun deleteRecipe(id: String) = repository.deleteById(id)

    fun getRecipe(id: String): Recipe = repository.findById(id) ?: throw NotFoundException(id)
some bdds
    fun filterRecipes(keywords: List<String>): List<Recipe> {
        data class PrioritizedRecipe(val priority: Int, val recipe: Recipe)
        return getAllRecipes()
            .flatMap { recipe ->
                val priority = calculatePriority(keywords, recipe)
                if (priority == 0)
                    emptyList()
                else
                    listOf(PrioritizedRecipe(priority, recipe))
            }
            .sortedBy { -it.priority }
            .map { it.recipe }
    }

    private fun calculatePriority(keywords: List<String>, recipe: Recipe): Int {
        var priority = 0
        keywords.forEach { keyword ->
            if (recipe.name.contains(keyword))
                priority += 10
            if (recipe.ingredientGroups.any { group -> group.ingredients.any { it.name.contains(keyword) } })
                priority += 2
            if (recipe.instructions.any { it.contains(keyword) })
                priority += 2
        }
        return priority
    }

    fun findSimilarRecipes(id: String): List<Recipe> {
        val recipe = repository.findById(id) ?: throw NotFoundException(id)

        val keywords = mutableListOf<String>().apply {
            addAll(recipe.name.split(" "))
            addAll(recipe.ingredientGroups.flatMap { g ->
                g.ingredients.map { getKeywordFromIngredient(it) }
            })
        }

        return filterRecipes(keywords).filter {
            it.id != recipe.id
        }.take(6)
    }

    private fun getKeywordFromIngredient(ingredient: Ingredient): String {
        val words = ingredient.name.split(" ")
        return words.last()
    }
}