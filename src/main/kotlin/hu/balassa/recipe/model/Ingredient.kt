package hu.balassa.recipe.model

import javax.persistence.*

@Embeddable
class Ingredient {
    @Column(precision = 4, scale = 2)
    var quantity: Double? = null

    @Column(precision = 4, scale = 2)
    var quantity2: Double? = null

    @Column(nullable = false)
    lateinit var name: String
}