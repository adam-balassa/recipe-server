package hu.balassa.recipe.service

import com.fasterxml.jackson.databind.ObjectMapper
import hu.balassa.recipe.dto.NewStreetKitchenRecipe
import hu.balassa.recipe.exception.NotFoundException
import hu.balassa.recipe.model.Category
import hu.balassa.recipe.model.Ingredient
import hu.balassa.recipe.model.IngredientGroup
import hu.balassa.recipe.model.Recipe
import hu.balassa.recipe.repository.RecipeRepository
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.model.CSVInput
import software.amazon.awssdk.services.s3.model.JSONInput
import java.io.File
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path

@Service
class RecipeService(
        private val repository: RecipeRepository,
        private val imageUploadClient: ImageUploadClient,
        private val streetKitchenService: StreetKitchenService
) {

    fun migrateAllRecipes() {
        val files = mapOf(
            "recipes" to "d10t11uakcvqtr_public_recipes.json",
            "instructions" to "d10t11uakcvqtr_public_instructions.json",
            "ingredientGroups" to "d10t11uakcvqtr_public_ingredient_groups.json",
            "ingredients" to "d10t11uakcvqtr_public_ingredients.json"
        )
        val recipeNodes = ObjectMapper().readTree(Path.of("data", files["recipes"]).toFile())
        val instructionNodes = ObjectMapper().readTree(Path.of("data", files["instructions"]).toFile())
        val ingredientGroupNodes = ObjectMapper().readTree(Path.of("data", files["ingredientGroups"]).toFile())
        val ingredientNodes = ObjectMapper().readTree(Path.of("data", files["ingredients"]).toFile())

        val recipes: Map<String, Recipe> = recipeNodes.fold (mutableMapOf()){ map, recipeNode ->
            val recipe = Recipe().apply {
                id = recipeNode["id"].asText()
                name = recipeNode["name"].asText()
                imageUrl = recipeNode["image_url"].asText()
                quantity = recipeNode["quantity"].asInt()
                quantity2 = recipeNode["quantity2"].asInt()
                instructions = emptyList()
                category = Category.values()[recipeNode["category"].asInt()]
            }
            map.apply { put(recipe.id!!, recipe) }
        }

        instructionNodes.forEach {
            val recipe = recipes[it["recipe_id"].asText()]!!
            recipe.instructions += it["instructions"].asText()
        }

        val ingredientGroups: Map<Int, Pair<Int, IngredientGroup>> = ingredientGroupNodes.fold(mutableMapOf()) {
                map, it ->
            val ingredientGroup = IngredientGroup().apply {
                name = it["name"].asText()
                ingredients = emptySet()
            }
            val nextValue = it["recipe_id"].asInt() to ingredientGroup
            val id = it["id"].asInt()
            map.apply { put(id, nextValue) }
        }

        ingredientNodes.forEach {
            val group = ingredientGroups[it["group_id"].asInt()]!!.second
            group.ingredients += Ingredient().apply {
                name = it["name"].asText()
                quantity = it["quantity"].asDouble()
                quantity2 = it["quantity2"].asDouble()
            }
        }

        ingredientGroups.forEach { _, (recipeId, ingredientGroup) ->
            if (recipeId != 0) {
                val recipe = recipes[recipeId.toString()]!!
                recipe.ingredientGroups += ingredientGroup
            }
        }

        recipes.forEach { _, recipe ->
            repository.save(recipe)
        }
    }

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

    fun filterRecipes(kw: List<String>): List<Recipe> = getAllRecipes()
//    = criteriaQuery(em) { root, cb ->
//        val keywords = kw.map { it.decapitalize() }
//        val ingredientGroups = root.join<Recipe, IngredientGroup>("ingredientGroups")
//        val ingredients = ingredientGroups.join<IngredientGroup, Ingredient>("ingredients")
//
//        val emptyPredicate = cb.isTrue(cb.literal(true))
//        val filter = keywords.fold(emptyPredicate) { predicate, keyword ->
//            cb.and(predicate, cb.or(
//                    cb.like(ingredients.get("name"), "%$keyword%"),
//                    cb.like(root.get("name"), "%${keyword}%")
//            ))
//        }
//        where(filter)
//    }
//
//    fun findRecipesByKeywords(kw: List<String>): List<Recipe> = criteriaQuery(em) { root, cb ->
//        val keywords = kw.map { it.decapitalize() }
//        val ingredientGroups = root.join<Recipe, IngredientGroup>("ingredientGroups")
//        val ingredients = ingredientGroups.join<IngredientGroup, Ingredient>("ingredients")
//
//        val emptyPredicate = cb.isTrue(cb.literal(false))
//        val filter = keywords.fold(emptyPredicate) { predicate, keyword ->
//            cb.or(predicate, cb.or(
//                    cb.like(ingredients.get("name"), "%$keyword%"),
//                    cb.like(root.get("name"), "%${keyword}%")
//            ))
//        }
//        where(filter)
//    }

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