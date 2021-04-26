package hu.balassa.recipe.stepdefs

import hu.balassa.recipe.helpers.DynamoHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.reactive.server.WebTestClient

abstract class BaseStepDef {
    @Autowired
    protected lateinit var web: WebTestClient

    @Autowired
    protected lateinit var dynamo: DynamoHelper

}