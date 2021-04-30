package hu.balassa.recipe.stepdefs

import hu.balassa.recipe.exception.ErrorResponse
import hu.balassa.recipe.helpers.RecipeHelper
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import org.assertj.core.api.Assertions

class CommonSteps: BaseStepDef() {
    @Given("there (is)(are) {int} recipe(s) in the database")
    fun initializeDBWithOneItem(numberOfRecipes: Int) {
        dynamo.withRecipes(MutableList(numberOfRecipes) {
            RecipeHelper.recipeOf(id = "testId$it")
        })
    }

    @Given("the following recipes are in the database")
    fun initializeRecipes(data: DataTable) {
        val recipes = data.asMaps<String, String>(String::class.java, String::class.java)
        dynamo.withRecipes (recipes.map {
            RecipeHelper.recipeOf(id = it["id"]!!, name = it["name"]!!)
        })
    }


    @Then("I receive a bad request error")
    fun expectErrorResponse() {
        val error = response
            .expectStatus().isBadRequest
            .expectBody(ErrorResponse::class.java)
            .returnResult()
            .responseBody
        Assertions.assertThat(error).isNotNull
        Assertions.assertThat(error!!.error).isEqualTo("Bad request")
        Assertions.assertThat(error.reason).isNotNull
    }
}