package hu.balassa.recipe.model

import hu.balassa.recipe.model.Category.MAIN
import hu.balassa.recipe.service.mapping.RecipeIngredientConverter
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbConvertedBy
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey

@DynamoDbBean
class Recipe {
    @get:DynamoDbPartitionKey
    var id: String? = null

    lateinit var name: String

    var imageUrl: String? = null

    var quantity: Int = 0

    var quantity2: Int? = null

    @get: DynamoDbConvertedBy(RecipeIngredientConverter::class)
    var ingredientGroups: Set<IngredientGroup> = emptySet()

    lateinit var instructions: List<String>

    var category: Category = MAIN
}

enum class Category {
    MAIN, BREAKFAST, DESSERT, OTHER
}