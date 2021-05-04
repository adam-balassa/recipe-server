package hu.balassa.recipe.service

import hu.balassa.recipe.exception.UnsuccessfulRequestException
import hu.balassa.recipe.model.Ingredient
import hu.balassa.recipe.model.IngredientGroup
import hu.balassa.recipe.model.Recipe
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.springframework.stereotype.Service

interface StreetKitchenService {
    fun getRecipe(url: String): Recipe
}

@Service
class StreetKitchenServiceImpl: StreetKitchenService {
    override fun getRecipe(url: String): Recipe {
        val document = getHTMLContent(url)
        val ingredientGroups = getIngredientGroups(document)
        val recipeName = document.select("h1").first().text()
        val imageUrl = document.select(".article-featured-image-bg noscript img").attr("src").ifEmpty {
                    document.select(".the-content-div figure noscript img").attr("src")
                }
        val instructions = getInstructions(document)
        val quantity = document.select(".quantity .quantity-number")?.first()?.text()?.toInt() ?: 2
        val quantity2 = document.select(".quantity .quantity2-number").first()?.text()?.toIntOrNull()

        return Recipe().also {
            it.name = recipeName
            it.imageUrl = imageUrl
            it.ingredientGroups = ingredientGroups
            it.instructions = instructions
            it.quantity = quantity
            it.quantity2 = quantity2
        }
    }

    private fun getHTMLContent(url: String): Document {
        return Jsoup.connect(url).get() ?: throw UnsuccessfulRequestException()
    }

    private fun getIngredientGroups(document: Document): List<IngredientGroup> {
        val groupElements = document.select(".article-content .ingredients-left .ingredients-content .ingredient-groups .ingredient-group")
        val ingredientGroups = mutableListOf<IngredientGroup>()
        for (group in groupElements) {
            val groupName = group.select("h3").text()
            val ingredients = getIngredients(group)
            ingredientGroups.add(IngredientGroup().also { it.name = groupName; it.ingredients = ingredients })
        }
        return ingredientGroups
    }

    private fun getIngredients(group: Element): List<Ingredient> {
        val ingredients = mutableListOf<Ingredient>()
        for (ingredientElement in group.select("dd")) {
            val quantityString = ingredientElement.select(".ig-quantity").text()
            val quantity = quantityString.toQuantity()

            val quantityString2 = ingredientElement.select(".ig-quantity2").text()
            val quantity2 = quantityString2.toQuantity()

            val name = ingredientElement.textNodes().last().text().removePrefix(" ")

            ingredients.add(Ingredient().also { it.quantity = quantity; it.quantity2 = quantity2; it.name = name })
        }
        return ingredients
    }

    private fun getInstructions(document: Document): List<String> {
        val paragraphs = document.select(".the-content-div p")
        return paragraphs.map { it.text() }.fold(mutableListOf<String>() to false) { acc, current ->
            when {
                acc.second -> acc
                current.substring(0 until 15).contains("Ha tetszett a") -> acc.first to true
                else -> {
                    acc.first.add(current)
                    acc.first to false
                }
            }
        }.first
    }

    private fun String.toQuantity(): Double? {
        return when {
            this == "½" -> 0.5
            else -> toDoubleOrNull()
        }
    }
}