package hu.balassa.recipe.dto

class RecipeDto {
    var id: Long? = null
    lateinit var name: String
    var imageUrl: String? = null
    var quantity: Int = 0
    var quantity2: Int? = null
    lateinit var ingredientGroups: List<IngredientGroupDto>
    lateinit var instructions: List<String>
}