package hu.balassa.recipe.model

class Ingredient {
    var quantity: Double? = null
    var quantity2: Double? = null
    lateinit var name: String
    override fun toString(): String {
        return "Ingredient(quantity=$quantity, quantity2=$quantity2, name='$name')"
    }
}