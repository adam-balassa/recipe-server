package hu.balassa.recipe.controller

import hu.balassa.recipe.dto.ImageUrl
import hu.balassa.recipe.dto.NewStreetKitchenRecipe
import hu.balassa.recipe.dto.RecipeDto
import hu.balassa.recipe.dto.RecipeHeader
import hu.balassa.recipe.service.ImageUploadClient
import hu.balassa.recipe.service.RecipeService
import hu.balassa.recipe.service.mapping.DtoMapper
import hu.balassa.recipe.util.Util
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files

@RestController
@RequestMapping("/recipe")
@CrossOrigin(origins=["*"])
class RecipeController (
        private val service: RecipeService,
        private val imageUploadClient: ImageUploadClient
) {
    @GetMapping
    fun listRecipes(): List<RecipeHeader> = service.getAllRecipes().map {
        DtoMapper.recipeToHeaderDto(it)
    }

    @PostMapping("/image", consumes = ["multipart/form-data"])
    fun uploadFile(@RequestParam("imageFile") image: MultipartFile): ImageUrl {
        val bytes = image.bytes
        val path = Files.createTempFile(Util.generateUUID(), ".jpg")
        Files.write(path, bytes)
        val url = imageUploadClient.uploadImageFromFile(path.toFile())
        return ImageUrl(url)
    }

    @GetMapping("/{id}")
    fun getRecipe(@PathVariable("id") id: String): RecipeDto = service.getRecipe(id).let {
        DtoMapper.recipeToDto(it)
    }

    @GetMapping("/filter")
    fun searchRecipe(@RequestParam keywords: List<String>) = service.filterRecipes(keywords).map {
        DtoMapper.recipeToHeaderDto(it)
    }

    @PostMapping
    fun addRecipe(@RequestBody recipe: RecipeDto) = DtoMapper.recipeToDto(
            service.saveRecipe(DtoMapper.recipeToModel(recipe).apply { id = null })
    )

    @PutMapping("/{id}")
    fun updateRecipe(@RequestBody recipe: RecipeDto, @PathVariable("id") id: String) = DtoMapper.recipeToDto(
            service.saveRecipe(DtoMapper.recipeToModel(recipe).also { it.id = id })
    )

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteRecipe(@PathVariable("id") id: String) = service.deleteRecipe(id)


    @PostMapping("/streetkitchen")
    fun addStreetKitchenRecipe(@RequestBody info: NewStreetKitchenRecipe): RecipeDto {
        val recipe = service.addStreetKitchenRecipe(info)
        return DtoMapper.recipeToDto(recipe)
    }
}