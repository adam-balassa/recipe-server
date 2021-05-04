package hu.balassa.recipe.dto

import com.fasterxml.jackson.annotation.JsonProperty
import hu.balassa.recipe.model.Category
import org.hibernate.validator.constraints.URL
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class RecipeHeader (
    val id: String?,
    @field: NotEmpty
    val name: String,
    @field: URL
    @field: NotEmpty
    val imageUrl: String?,
    @field: NotNull
    val quantity: Int,
    val quantity2: Int?,
    @field: NotNull
    @field: JsonProperty("isVegetarian")
    val vegetarian: Boolean,
    @field: NotEmpty
    val category: Category
)