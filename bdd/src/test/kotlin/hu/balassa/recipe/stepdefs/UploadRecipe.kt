package hu.balassa.recipe.stepdefs

import hu.balassa.recipe.dto.RecipeDto
import hu.balassa.recipe.exception.ErrorResponse
import hu.balassa.recipe.helpers.RecipeHelper.recipeOf
import hu.balassa.recipe.helpers.RecipeHelper.verifyRecipe
import hu.balassa.recipe.model.Recipe
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions.assertThat

class UploadRecipe: BaseStepDef() {
    @When("I upload the following recipe")
    fun uploadRecipe(data: DataTable) {
        val recipe = recipeOf(data.asMap(String::class.java, String::class.java))
        response = web.post().uri("/recipe")
            .bodyValue(recipe)
            .exchange()
    }

    @Then("I receive a response of the following recipe")
    fun checkRecipeResponse(data: DataTable) {
        val recipe = response.expectStatus().isCreated
            .expectBody(RecipeDto::class.java)
            .returnResult()
            .responseBody

        assertThat(recipe).isNotNull
        val expectedRecipe = recipeOf(data.asMap(String::class.java, String::class.java))
        verifyRecipe(recipe!!, expectedRecipe)
    }


    @When("I try to upload an invalid recipe")
    fun uploadRecipe() {
        response = web.post().uri("/recipe")
            .bodyValue(object {
                val invalid = "property"
            })
            .exchange()
    }

    @Then("the result recipe's image is stored on amazon")
    fun checkRecipeImage() {
        val result = response
            .expectStatus().isCreated
            .expectBody(Recipe::class.java)
            .returnResult().responseBody

        assertThat(result!!.imageUrl).contains("amazonaws.com")
    }
}