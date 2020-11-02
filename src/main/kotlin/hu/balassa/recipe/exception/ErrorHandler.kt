package hu.balassa.recipe.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ErrorHandler {
    @ExceptionHandler(IllegalArgumentException::class)
    fun badRequest(exception: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
                ErrorResponse("Bad request", exception.localizedMessage ?: "Illegal arguments provided"),
                HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(UnsuccessfulRequestException::class)
    fun unprocessableEntity(): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
                ErrorResponse("Unsuccessful request", "Request failed to Street Kitchen"),
                HttpStatus.UNPROCESSABLE_ENTITY
        )
    }
}

data class ErrorResponse(
        val error: String,
        val reason: String
)
