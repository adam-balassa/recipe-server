package hu.balassa.recipe.stepdefs

import hu.balassa.recipe.helpers.DynamoHelper
import hu.balassa.recipe.helpers.RecipeHelper
import hu.balassa.recipe.helpers.RecipeHelper.recipeOf
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.reactive.server.WebTestClient

abstract class BaseStepDef {
    @Autowired
    protected lateinit var web: WebTestClient

    @Autowired
    protected lateinit var dynamo: DynamoHelper
}