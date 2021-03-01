package hu.balassa.recipe.model

class IngredientGroup {
    var name: String? = null
    lateinit var ingredients: Set<Ingredient>
    override fun toString(): String {
        return "IngredientGroup(name=$name, ingredients=$ingredients)"
    }
}