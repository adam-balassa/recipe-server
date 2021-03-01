package hu.balassa.recipe.repository

import hu.balassa.recipe.config.DynamoDbConfig
import hu.balassa.recipe.model.Recipe
import hu.balassa.recipe.util.Util
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import java.util.*

@Repository
class RecipeRepository(
    db: DynamoDbEnhancedClient
) {
    companion object {
        private val tableSchema = TableSchema.fromBean(Recipe::class.java)
    }

    private val table by lazy {
        db.table(DynamoDbConfig.tableName, tableSchema)
    }


    fun findAll(): List<Recipe> = table.scan().items().toList()

    fun save(recipe: Recipe): Recipe {
        recipe.id = Util.generateUUID()
        table.putItem(recipe)
        return recipe
    }

    fun deleteById(id: String) {
        table.deleteItem(
            Key.builder().partitionValue(id).build()
        )
    }

    fun findById(id: String): Recipe? =
        table.getItem(
            Key.builder().partitionValue(id).build()
        )
}