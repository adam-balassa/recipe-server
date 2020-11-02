package hu.balassa.recipe.dto

class IngredientGroupDto {
    var name: String? = null
    lateinit var ingredients: List<IngredientDto>
}