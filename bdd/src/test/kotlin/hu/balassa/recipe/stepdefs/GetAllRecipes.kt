package hu.balassa.recipe.stepdefs

import hu.balassa.recipe.dto.RecipeHeader
import hu.balassa.recipe.helpers.DynamoHelper
import hu.balassa.recipe.helpers.RecipeHelper.recipeOf
import hu.balassa.recipe.model.Recipe
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.reactive.server.WebTestClient

class GetAllRecipes: BaseStepDef() {
    private lateinit var resultList: List<RecipeHeader>

    @Given("there (is)(are) {int} recipe(s) in the database")
    fun initializeDBWithOneItem(numberOfRecipes: Int) {
        dynamo.withRecipes(MutableList(numberOfRecipes) {
            recipeOf(id = "testId$it")
        })
    }

    @When("I request getting all recipes")
    fun getRecipesRequest() {
        val result = web.get().uri("/recipe").exchange()
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
}