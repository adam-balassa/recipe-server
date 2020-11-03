package hu.balassa.recipe.dto

open class RecipeHeader {
    var id: Long? = null
    lateinit var name: String
    var imageUrl: String? = null
    var quantity: Int = 0
    var quantity2: Int? = null
}