package hu.balassa.recipe.dto

import hu.balassa.recipe.model.Category

open class RecipeHeader {
    var id: String? = null
    lateinit var name: String
    var imageUrl: String? = null
    var quantity: Int = 0
    var quantity2: Int? = null
    lateinit var category: Category
}