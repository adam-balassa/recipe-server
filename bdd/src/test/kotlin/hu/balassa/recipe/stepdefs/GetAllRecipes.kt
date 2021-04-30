package hu.balassa.recipe.stepdefs

import hu.balassa.recipe.dto.RecipeHeader
import hu.balassa.recipe.helpers.DynamoHelper
import hu.balassa.recipe.helpers.RecipeHelper
import hu.balassa.recipe.helpers.RecipeHelper.recipeOf
import hu.balassa.recipe.model.Recipe
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.reactive.server.WebTestClient

class GetAllRecipes: BaseStepDef() {
    private lateinit var resultList: List<RecipeHeader>

    @When("I request getting all recipes")
    fun getRecipesRequest() {
        val result = web.get().uri("/recipe").exchange()
            .expectStatus().isOk
            .expectBodyList(RecipeHeader::class.java)
            .returnResult()
            .responseBody
        assertThat(result).isNotNull
        resultList = result!!
    }

    @Then("I receive list of {int} recipe(s)")
    fun receiveOneItemList(numberOfRecipes: Int) {
        assertThat(resultList).hasSize(numberOfRecipes)
        assertThat(resultList).allSatisfy {
            assertThat(it.id).isNotNull
            assertThat(it.name).isNotNull
            assertThat(it.isVegetarian).isNotNull
            assertThat(it.category).isNotNull
            assertThat(it.quantity).isNotNull
        }
    }

    @Then("the recipes ids match")
    fun checkIds() {
        assertThat(resultList).allSatisfy {
            assertThat(it.id).matches("^testId\\d$")
        }
    }

    @Then("I receive the following recipes")
    fun checkForRecipes(data: DataTable) {
        val recipes = data.asMaps<String, String>(String::class.java, String::class.java)
        assertThat(resultList).hasSize(recipes.size)
        recipes.forEach { recipeData ->
            assertThat(resultList).anySatisfy {
                assertThat(it.id).isEqualTo(recipeData["id"])
                assertThat(it.name).isEqualTo(recipeData["name"])
            }
        }
    }
}