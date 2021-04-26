package hu.balassa.recipe.stepdefs

import hu.balassa.recipe.model.Category
import hu.balassa.recipe.model.Recipe
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions.assertThat
import java.lang.Integer.parseInt

class DownloadRecipe: BaseStepDef() {
    lateinit var result: Recipe

    @When("I download a Street Kitchen recipe from {word}")
    fun downloadRecipe(url: String) {
        val result = web.post().uri("/recipe/streetkitchen")
            .bodyValue(object { val url = url })
            .exchange()
            .expectStatus().isCreated
            .expectBody(Recipe::class.java)
            .returnResult()
            .responseBody

        this.result = result!!
    }

    @Then("I receive the following recipe")
    fun checkResult(data: DataTable) {
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
}