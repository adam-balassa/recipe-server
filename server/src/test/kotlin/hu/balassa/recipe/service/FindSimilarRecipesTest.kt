package hu.balassa.recipe.service

import hu.balassa.recipe.helper.RecipeHelper.loadRecipe
import hu.balassa.recipe.repository.RecipeRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.whenever

class FindSimilarRecipesTest {
    companion object {
        private const val TEST_ID = "testId"
    }
    private val repository: RecipeRepository = spy()
    private val service: RecipeService = RecipeService(
        repository, mock(), mock()
    )

    @Test
    fun findSimilarRecipes() {
        val (recipe1, recipe2) = loadRecipe("recipe1") to loadRecipe("recipe2")
        whenever(repository.findAll()).thenReturn(listOf(recipe1, recipe2))
        whenever(repository.findById(TEST_ID)).thenReturn(recipe1)

        val response = service.findSimilarRecipes(TEST_ID)

        assertThat(response).hasSize(1)
    }


    @Test
    fun findSimilarRecipesOrder() {
        val recipe1 = loadRecipe("recipe1")
        val recipe2 = loadRecipe("recipe2")
        val recipe3 = loadRecipe("recipe3")
        val recipe4 = loadRecipe("recipe4")
        whenever(repository.findAll()).thenReturn(listOf(recipe1, recipe2, recipe3, recipe4))
        whenever(repository.findById(TEST_ID)).thenReturn(recipe1)

        val response = service.findSimilarRecipes(TEST_ID)

        assertThat(response).hasSize(3)
        assertThat(response[0].id).isEqualTo(recipe2.id)
    }
}