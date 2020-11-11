package hu.balassa.recipe.service

import hu.balassa.recipe.dto.NewStreetKitchenRecipe
import hu.balassa.recipe.dto.RecipeHeader
import hu.balassa.recipe.exception.NotFoundException
import hu.balassa.recipe.model.Ingredient
import hu.balassa.recipe.model.IngredientGroup
import hu.balassa.recipe.model.Recipe
import hu.balassa.recipe.repository.RecipeRepository
import org.springframework.stereotype.Service
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.*

@Service
class RecipeService(
        private val repository: RecipeRepository,
        private val streetKitchenService: StreetKitchenService
) {
    @PersistenceContext
    private lateinit var em: EntityManager

    fun getAllRecipes(): List<Recipe> = repository.findAll()

    fun saveRecipe(recipe: Recipe): Recipe = repository.save(recipe)

    fun addStreetKitchenRecipe(info: NewStreetKitchenRecipe): Recipe {
        val recipe = streetKitchenService.getRecipe(info.url)
        return repository.save(recipe)
    }

    fun deleteRecipe(id: Long) = repository.deleteById(id)

    fun getRecipe(id: Long): Recipe = repository.findWithDetails(id) ?: throw NotFoundException(id)

    fun filterRecipes(keywords: List<String>): List<Recipe> = criteriaQuery(em) { root, cb ->
        val ingredientGroups = root.join<Recipe, IngredientGroup>("ingredientGroups")
        val ingredients = ingredientGroups.join<IngredientGroup, Ingredient>("ingredients")

        val emptyPredicate = cb.isTrue(cb.literal(true))
        val filter = keywords.fold(emptyPredicate) { predicate, keyword ->
            cb.and(predicate, cb.or(
                    cb.like(ingredients.get("name"), "%$keyword%"),
                    cb.like(root.get("name"), "%${keyword}%")
            ))
        }
        where(filter)
    }

    private inline fun <reified ResultT> criteriaQuery(
            em: EntityManager,
            buildQuery: CriteriaQuery<ResultT>.(root: Root<ResultT>, cb: CriteriaBuilder) -> Unit
    ): List<ResultT> {

        val cb = em.criteriaBuilder
        val cr = cb.createQuery(ResultT::class.java).distinct(true)
        val root = cr.from(ResultT::class.java)

        cr.buildQuery(root, cb)
        val query = em.createQuery(cr)
        return query.resultList
    }
}