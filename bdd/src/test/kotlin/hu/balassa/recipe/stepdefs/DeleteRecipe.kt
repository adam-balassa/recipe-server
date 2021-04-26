package hu.balassa.recipe.stepdefs

import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec

class DeleteRecipe: BaseStepDef() {
    private lateinit var response: ResponseSpec

    @When("I delete a recipe with the id {word}")
    fun deleteRecipe(id: String) {
        response = web.delete().uri("/recipe/$id").exchange()
    }

    @Then("it succeeds")
    fun checkResponseStatus() {
        response.expectStatus().isNoContent
    }
}