package hu.balassa.recipe.dto

class RecipeDto: RecipeHeader() {
    lateinit var ingredientGroups: List<IngredientGroupDto>
    lateinit var instructions: List<String>
}