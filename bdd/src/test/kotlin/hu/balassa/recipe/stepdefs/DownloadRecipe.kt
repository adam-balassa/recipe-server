package hu.balassa.recipe.stepdefs

import hu.balassa.recipe.exception.ErrorResponse
import hu.balassa.recipe.model.Category
import hu.balassa.recipe.model.Recipe
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions.assertThat
import org.mockito.BDDMockito.*
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.springframework.test.web.reactive.server.WebTestClient
import software.amazon.awssdk.services.s3.model.PutObjectAclResponse
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectResponse
import java.lang.Integer.parseInt
import java.net.URI
import java.nio.file.Path

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
            .expectBody(Recipe::class.java)
            .returnResult()
            .responseBody!!

        val recipe = data.asMap<String, String>(String::class.java, String::class.java)
        assertThat(result.id).isNotNull
        assertThat(result.imageUrl).contains("amazonaws.com")
        assertThat(result.name).isEqualTo(recipe["name"])
        assertThat(result.category).isEqualTo(Category.MAIN)
        assertThat(result.ingredientGroups).hasSize(parseInt(recipe["ingredientSize"]))
        assertThat(result.instructions).hasSize(parseInt(recipe["instructionSize"]))
        assertThat(result.isVegetarian).isFalse
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