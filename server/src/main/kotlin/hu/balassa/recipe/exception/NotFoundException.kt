package hu.balassa.recipe.exception

import java.lang.RuntimeException

class NotFoundException(val id: String): RuntimeException() {
}