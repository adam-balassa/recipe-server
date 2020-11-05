package hu.balassa.recipe.model

import hu.balassa.recipe.model.Category.MAIN
import javax.persistence.*
import javax.persistence.CascadeType.ALL
import javax.persistence.FetchType.EAGER
import javax.persistence.FetchType.LAZY

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

    @OneToMany(cascade = [ALL], fetch = LAZY)
    @JoinColumn(name = "recipe_id")
    var ingredientGroups: Set<IngredientGroup> = emptySet()

    @ElementCollection(fetch = EAGER)
    @CollectionTable(name = "instructions", joinColumns = [JoinColumn(name = "recipe_id")])
    @Column(length = 800)
    lateinit var instructions: List<String>

    @Enumerated
    @Column(nullable=false)
    var category: Category = MAIN
}

enum class Category {
    MAIN, BREAKFAST, DESSERT, OTHER
}