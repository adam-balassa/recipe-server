package hu.balassa.recipe.stepdefs

import hu.balassa.recipe.exception.ErrorResponse
import hu.balassa.recipe.helpers.RecipeHelper.recipeOf
import hu.balassa.recipe.helpers.RecipeHelper.verifyRecipe
import hu.balassa.recipe.model.Category
import hu.balassa.recipe.model.Recipe
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions.assertThat
import org.springframework.test.web.reactive.server.WebTestClient

class GetRecipeDetails: BaseStepDef() {
    @Given("the following recipe is in the database")
    fun initializeDBWithRecipe(data: DataTable) {
        val recipe = data.asMap<String, String>(String::class.java, String::class.java)
        dynamo.withRecipes(listOf(recipeOf(recipe)))
    }


    @When("I retrieve recipe with id {word}")
    fun getRecipeById(id: String) {
        response = web.get().uri("/recipe/$id").exchange()
    }

    @Then("I receive the following recipe details")
    fun recipeSuccess(data: DataTable) {
        val expectedRecipe = recipeOf(data.asMap(String::class.java, String::class.java))
        val actualRecipe = response
            .expectStatus().isOk
            .expectBody(Recipe::class.java)
            .returnResult()
            .responseBody
        assertThat(actualRecipe).isNotNull
        verifyRecipe(actualRecipe!!, expectedRecipe)
    }

    @Then("I receive a recipe not found error")
    fun recipeNotFound() {
        val error = response
            .expectStatus().isNotFound
            .expectBody(ErrorResponse::class.java)
            .returnResult()
            .responseBody
        assertThat(error).isNotNull
        assertThat(error!!.error).isEqualTo("Not found")
        assertThat(error.reason).isNotNull
    }
}