package hu.balassa.recipe.model

import javax.persistence.*

@Entity
@Table(name ="recipes")
class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false)
    lateinit var name: String
}