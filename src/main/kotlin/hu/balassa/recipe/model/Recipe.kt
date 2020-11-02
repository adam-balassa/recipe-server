package hu.balassa.recipe.model

import javax.persistence.*
import javax.persistence.CascadeType.ALL
import javax.persistence.FetchType.EAGER

@Entity
@Table(name = "recipes")
class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false, length = 40)
    lateinit var name: String

    @Column(length = 150)
    var imageUrl: String? = null

    var quantity: Int = 0
    var quantity2: Int? = null

    @OneToMany(cascade = [ALL], fetch = EAGER)
    @JoinColumn(name = "recipe_id")
    lateinit var ingredientGroups: Set<IngredientGroup>

    @ElementCollection(fetch = EAGER)
    @CollectionTable(name = "instructions", joinColumns = [JoinColumn(name = "recipe_id")])
    @Column(length = 800)
    lateinit var instructions: List<String>
}