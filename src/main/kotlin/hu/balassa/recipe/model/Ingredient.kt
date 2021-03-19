package hu.balassa.recipe.model

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean

@DynamoDbBean
class Ingredient {
    var quantity: Double? = null
    var quantity2: Double? = null
    lateinit var name: String
    override fun toString(): String {
        return "Ingredient(quantity=$quantity, quantity2=$quantity2, name='$name')"
    }
}