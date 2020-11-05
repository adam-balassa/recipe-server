package hu.balassa.recipe.repository

import hu.balassa.recipe.model.Recipe
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RecipeRepository: JpaRepository<Recipe, Long> {
    @Query("select r from Recipe r join fetch r.ingredientGroups where r.id = :id")
    fun findWithDetails(@Param("id") id: Long): Recipe?
}