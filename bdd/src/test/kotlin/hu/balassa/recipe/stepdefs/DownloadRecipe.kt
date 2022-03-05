package hu.balassa.recipe.stepdefs

import hu.balassa.recipe.dto.RecipeDto
import hu.balassa.recipe.exception.ErrorResponse
import hu.balassa.recipe.model.Category
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions.assertThat
import java.lang.Integer.parseInt

class DownloadRecipe: BaseStepDef() {
    @When("I download a Street Kitchen recipe from {word}")
    fun downloadRecipe(url: String) {
        response = web.post().uri("/recipe/streetkitchen")
            .bodyValue(object { val url = url })
            .exchange()
    }

    @Then("I receive the following recipe")
    fun checkResult(data: DataTable) {
        val result = response
            .expectStatus().isCreated
            .expectBody(RecipeDto::class.java)
            .returnResult()
            .responseBody!!

        val recipe = data.asMap<String, String>(String::class.java, String::class.java)
        assertThat(result.id).isNotNull
        assertThat(result.imageUrl).contains("amazonaws.com")
        assertThat(result.name).isEqualTo(recipe["name"])
        assertThat(result.category).isEqualTo(Category.MAIN)
        assertThat(result.ingredientGroups).hasSize(parseInt(recipe["ingredientSize"]))
        assertThat(result.instructions).hasSize(parseInt(recipe["instructionSize"]))
        assertThat(result.vegetarian).isFalse
        assertThat(result.quantity).isEqualTo(parseInt(recipe["quantity"]))
        assertThat(result.quantity2).isEqualTo(parseInt(recipe["quantity2"]))
    }

    @Then("I receive an invalid URL error response")
    fun invalidURL() {
        val error = response
            .expectStatus().isBadRequest
            .expectBody(ErrorResponse::class.java)
            .returnResult().responseBody
        assertThat(error).isNotNull
        assertThat(error!!.error).isEqualTo("Bad request")
        assertThat(error.reason).isNotNull
    }
}