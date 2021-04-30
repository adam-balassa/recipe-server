package hu.balassa.recipe.dto

import org.hibernate.validator.constraints.URL
import javax.validation.constraints.NotEmpty

data class NewStreetKitchenRecipe (
    @field: NotEmpty
    @field: URL
    val url: String
)