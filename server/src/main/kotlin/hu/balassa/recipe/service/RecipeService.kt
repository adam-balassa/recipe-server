package hu.balassa.recipe.service

import hu.balassa.recipe.dto.NewStreetKitchenRecipe
import hu.balassa.recipe.exception.NotFoundException
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

    fun filterRecipes(keywords: List<String>): List<Recipe> {
        data class PrioritizedRecipe(val priority: Int, val recipe: Recipe)
        return getAllRecipes()
            .flatMap { recipe ->
                var priority = 0
                keywords.forEach { keyword ->
                    if (recipe.name.contains(keyword))
                        priority += 10
                    if (recipe.ingredientGroups.any { group -> group.ingredients.any { it.name.contains(keyword) } })
                        priority += 2
                    if (recipe.instructions.any { it.contains(keyword) })
                        priority += 2
                }
                if (priority == 0)
                    emptyList()
                else
                    listOf(PrioritizedRecipe(priority, recipe))
            }
            .sortedBy { -it.priority }
            .map { it.recipe }
    }

    fun findSimilarRecipes(id: String): List<Recipe> {
        val recipe = repository.findById(id) ?: throw NotFoundException(id)

        val keywords = mutableListOf<String>().apply {
            addAll(recipe.name.split(" "))
        }

        return filterRecipes(keywords)
    }
//    fun findSimilarRecipes(id: Long): List<Recipe> {
//        val recipe = repository.findByIdOrNull(id)
//                ?: throw NotFoundException(id)
//        val keywords = recipe.name.split(" ").flatMap { it.split("-") }
//
//        val recipesByName = findRecipesByKeywords(keywords).filter {
//            it.id != recipe.id
//        }
//        if (recipesByName.size >= 3)
//            return recipesByName
//
//        return findSimilarRecipesByIngredients(id)
//
//    }
//
//    private fun findSimilarRecipesByIngredients(id: Long): List<Recipe> {
//        val ingredientGroups = repository.findById(id)?.ingredientGroups
//                ?: throw NotFoundException(id)
//        val ingredients = ingredientGroups.flatMap { it.ingredients }
//        val ingredientNames = ingredients.flatMap { ingredient ->
//            ingredient.name.split(" ").let {
//                if (ingredient.quantity == null || it.size == 1)
//                    it
//                else
//                    it.subList(1, it.size - 1)
//            }
//        }
//        return findRecipesByKeywords(ingredientNames)
//    }
//
//    private inline fun <reified ResultT> criteriaQuery(
//            em: EntityManager,
//            buildQuery: CriteriaQuery<ResultT>.(root: Root<ResultT>, cb: CriteriaBuilder) -> Unit
//    ): List<ResultT> {
//
//        val cb = em.criteriaBuilder
//        val cr = cb.createQuery(ResultT::class.java).distinct(true)
//        val root = cr.from(ResultT::class.java)
//
//        cr.buildQuery(root, cb)
//        val query = em.createQuery(cr)
//        return query.resultList
//    }
}