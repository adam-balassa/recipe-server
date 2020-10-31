package hu.balassa.recipe.repository

import hu.balassa.recipe.model.Recipe
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RecipeRepository: JpaRepository<Recipe, Long> {
}