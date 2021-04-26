package hu.balassa.recipe.helpers

import hu.balassa.recipe.config.DynamoDbConfig
import hu.balassa.recipe.model.Recipe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.TableSchema

@Component
class DynamoHelper {
    @Qualifier("amazonDynamoDB")
    @Autowired
    lateinit var db: DynamoDbEnhancedClient


    companion object {
        private val tableSchema = TableSchema.fromBean(Recipe::class.java)
    }

    private val table by lazy {
        db.table(DynamoDbConfig.tableName, tableSchema)
    }

    fun emptyTable() {
        table.scan().items().forEach {
            table.deleteItem(it)
        }
    }

    fun withRecipes(recipes: List<Recipe>) {
        emptyTable()
        recipes.forEach {
            table.putItem(it)
        }
    }
}