package hu.balassa.recipe.repository

import hu.balassa.recipe.config.DynamoDbConfig
import hu.balassa.recipe.model.Recipe
import hu.balassa.recipe.util.Util
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.Expression
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

interface RecipeRepository {
    fun findAll(): List<Recipe>

    fun save(recipe: Recipe): Recipe

    fun deleteById(id: String)

    fun findById(id: String): Recipe?

    fun findByNameContains(keywords: Collection<String>): List<Recipe>
}


@Repository
class RecipeRepositoryImpl(
    db: DynamoDbEnhancedClient
): RecipeRepository {
    companion object {
        private val tableSchema = TableSchema.fromBean(Recipe::class.java)
    }

    private val table by lazy {
        db.table(DynamoDbConfig.tableName, tableSchema)
    }

    override fun findAll(): List<Recipe> = table.scan().items().toList()

    override fun save(recipe: Recipe): Recipe {
        when (recipe.id) {
            null -> {
                recipe.id = Util.generateUUID()
                table.putItem(recipe)
            }
            else -> {
                table.updateItem(recipe)
            }
        }
        return recipe
    }

    override fun deleteById(id: String) {
        table.deleteItem(
            Key.builder().partitionValue(id).build()
        )
    }

    override fun findById(id: String): Recipe? =
        table.getItem(
            Key.builder().partitionValue(id).build()
        )

    override fun findByNameContains(keywords: Collection<String>) = table.scan {
        it.filterExpression(
            Expression.builder()
            .expression(keywords.mapIndexed { i, _ -> "contains(#recipeName, :queriedName$i)"}.joinToString(" or "))
            .expressionNames(mapOf("#recipeName" to "name"))
            .expressionValues(keywords.foldIndexed(mutableMapOf()) { i, map, keyword ->
                map.apply {
                    put(":queriedName$i", AttributeValue.builder().s(keyword).build())
                }
            })
            .build()
        )
    }.items().toList()
}