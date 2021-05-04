package hu.balassa.recipe.model

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean

@DynamoDbBean
class IngredientGroup {
    var name: String? = null
    lateinit var ingredients: List<Ingredient>
    override fun toString(): String {
        return "IngredientGroup(name=$name, ingredients=$ingredients)"
    }
}