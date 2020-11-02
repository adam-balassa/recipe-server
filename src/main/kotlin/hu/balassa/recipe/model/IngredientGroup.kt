package hu.balassa.recipe.model

import javax.persistence.*
import javax.persistence.FetchType.EAGER

@Entity
@Table(name ="ingredient_groups")
class IngredientGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    var name: String? = null

    @ElementCollection(fetch = EAGER)
    @CollectionTable(name="ingredients", joinColumns=[JoinColumn(name = "group_id")])
    lateinit var ingredients: Set<Ingredient>
}