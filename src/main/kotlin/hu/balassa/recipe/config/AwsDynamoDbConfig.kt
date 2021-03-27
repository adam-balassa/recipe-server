package hu.balassa.recipe.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentials
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.*
import software.amazon.awssdk.services.s3.S3Client
import java.net.URI

abstract class DynamoDbConfig {
    companion object {
        const val tableName = "recipe"
    }

    abstract fun dynamoDbClient(): DynamoDbClient

    open fun amazonDynamoDB(): DynamoDbEnhancedClient {
        val db = dynamoDbClient()

        if (!db.listTables().tableNames().contains(tableName)) {
            createTable(db)
        }

        return DynamoDbEnhancedClient.builder().dynamoDbClient(db).build()
    }

    private fun createTable(db: DynamoDbClient) {
        db.createTable(
            CreateTableRequest
                .builder()
                .tableName(tableName)
                .keySchema(KeySchemaElement.builder().attributeName("id").keyType(KeyType.HASH).build())
                .attributeDefinitions(
                    AttributeDefinition.builder().attributeName("id").attributeType(
                        ScalarAttributeType.S).build())
                .provisionedThroughput(ProvisionedThroughput.builder().readCapacityUnits(1L).writeCapacityUnits(1L).build())
                .build()
        )
    }
}

@Configuration
@Profile("develop")
class DynamoDbStaticConfig: DynamoDbConfig() {
    @Value("\${amazon.dynamodb.endpoint}")
    private lateinit var dbEndpoint: String

    @Value("\${amazon.dynamodb.tableName}")
    private lateinit var tableName: String

    @Value("\${amazon.aws.accesskey}")
    private lateinit var awsAccessKey: String

    @Value("\${amazon.aws.secretkey}")
    private lateinit var awsSecretKey: String

    @Bean
    override fun amazonDynamoDB(): DynamoDbEnhancedClient {
        return super.amazonDynamoDB()
    }

    override fun dynamoDbClient(): DynamoDbClient = DynamoDbClient
            .builder()
            .region(Region.EU_CENTRAL_1)
            .credentialsProvider { awsCredentials() }
            .endpointOverride(URI.create(dbEndpoint))
            .build()

    private fun awsCredentials(): AwsCredentials {
        return AwsBasicCredentials.create(awsAccessKey, awsSecretKey)
    }
}

@Configuration
@Profile("production")
class DynamoDbRoleConfig: DynamoDbConfig() {
    @Bean
    override fun amazonDynamoDB(): DynamoDbEnhancedClient {
        return super.amazonDynamoDB()
    }

    @Bean
    override fun dynamoDbClient(): DynamoDbClient = DynamoDbClient
        .builder()
        .region(Region.EU_CENTRAL_1)
        .build()
}