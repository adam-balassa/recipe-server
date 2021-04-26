package hu.balassa.recipe.stepdefs

import hu.balassa.recipe.helpers.RecipeHelper.recipeOf
import hu.balassa.recipe.model.Recipe
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions.assertThat

class UploadRecipe: BaseStepDef() {
    private lateinit var uploadedRecipe: Recipe

    @When("I upload a recipe")
    fun uploadRecipe() {
        val result = web.post().uri("/recipe")
            .bodyValue(recipeOf())
            .exchange()
            .expectStatus().isCreated
            .expectBody(Recipe::class.java)
            .returnResult()
            .responseBody

        assertThat(result).isNotNull
        uploadedRecipe = result!!
    }

    @Then("I receive a response of that recipe")
    fun checkRecipeResponse() {
        assertThat(uploadedRecipe.id).isNotNull
    }
}