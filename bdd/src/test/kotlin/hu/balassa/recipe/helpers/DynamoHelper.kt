package hu.balassa.recipe.helpers

import hu.balassa.recipe.config.DynamoDbConfig
import hu.balassa.recipe.dto.RecipeDto
import hu.balassa.recipe.model.Recipe
import hu.balassa.recipe.service.mapping.DtoModelMapper
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
    @Autowired
    lateinit var mapper: DtoModelMapper


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

    fun withRecipes(recipes: List<RecipeDto>) {
        emptyTable()
        recipes.forEach {
            table.putItem(mapper.recipeDtoToModel(it))
        }
    }
}