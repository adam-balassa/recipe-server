package hu.balassa.recipe.stepdefs

import hu.balassa.recipe.helpers.DynamoHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec
import software.amazon.awssdk.services.s3.S3Client

abstract class BaseStepDef {
    @Autowired
    protected lateinit var web: WebTestClient

    @Autowired
    protected lateinit var dynamo: DynamoHelper

    @Autowired
    protected lateinit var s3: S3Client

    companion object {
        @JvmStatic
        lateinit var response: ResponseSpec
    }
}