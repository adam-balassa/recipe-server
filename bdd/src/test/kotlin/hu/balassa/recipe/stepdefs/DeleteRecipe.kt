package hu.balassa.recipe.stepdefs

import io.cucumber.java.en.Then
import io.cucumber.java.en.When
class DeleteRecipe: BaseStepDef() {
    @When("I delete a recipe with the id {word}")
    fun deleteRecipe(id: String) {
        response = web.delete().uri("/recipe/$id").exchange()
    }

    @Then("it succeeds")
    fun checkResponseStatus() {
        response.expectStatus().isNoContent
    }
}