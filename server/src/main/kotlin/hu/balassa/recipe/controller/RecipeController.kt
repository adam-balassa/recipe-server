package hu.balassa.recipe.controller

import hu.balassa.recipe.dto.ImageUrl
import hu.balassa.recipe.dto.NewStreetKitchenRecipe
import hu.balassa.recipe.dto.RecipeDto
import hu.balassa.recipe.dto.RecipeHeader
import hu.balassa.recipe.service.ImageUploadClient
import hu.balassa.recipe.service.RecipeService
import hu.balassa.recipe.service.mapping.DtoModelMapper
import hu.balassa.recipe.util.Util
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import javax.validation.Valid

@RestController
@RequestMapping("/recipe")
@CrossOrigin(origins = ["*"])
class RecipeController(
    private val service: RecipeService,
    private val imageUploadClient: ImageUploadClient,
    private val mapper: DtoModelMapper
) {
    @GetMapping
    fun listRecipes(): List<RecipeHeader> =
        mapper.recipeModelsToHeaderDtos(service.getAllRecipes())

    @PostMapping("/image", consumes = ["multipart/form-data"])
    @ResponseStatus(CREATED)
    fun uploadFile(@RequestParam("imageFile") image: MultipartFile): ImageUrl {
        val bytes = image.bytes
        val path = Files.createTempFile(Util.generateUUID(), ".jpg")
        Files.write(path, bytes)
        val url = imageUploadClient.uploadImageFromFile(path.toFile())
        return ImageUrl(url)
    }

    @GetMapping("/{id}")
    fun getRecipe(@PathVariable("id") id: String): RecipeDto =
        service.getRecipe(id).let {
            mapper.recipeModelToDto(it)
        }

    @GetMapping("/filter")
    fun searchRecipe(@RequestParam keywords: List<String>) =
        mapper.recipeModelsToHeaderDtos(service.filterRecipes(keywords))

    @GetMapping("/{id}/similar")
    fun searchSimilarRecipes(@PathVariable("id") id: String) =
        mapper.recipeModelsToHeaderDtos(service.findSimilarRecipes(id))

    @PostMapping
    @ResponseStatus(CREATED)
    fun addRecipe(@RequestBody recipe: RecipeDto) =
        mapper.recipeModelToDto(
            service.saveRecipe(mapper.recipeDtoToModel(recipe).apply { id = null })
        )

    @PutMapping("/{id}")
    fun updateRecipe(@RequestBody recipe: RecipeDto, @PathVariable("id") id: String) =
        mapper.recipeModelToDto(
            service.saveRecipe(mapper.recipeDtoToModel(recipe).also { it.id = id })
        )

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    fun deleteRecipe(@PathVariable("id") id: String) =
        service.deleteRecipe(id)


    @PostMapping("/streetkitchen")
    @ResponseStatus(CREATED)
    fun addStreetKitchenRecipe(@Valid @RequestBody info: NewStreetKitchenRecipe): RecipeDto {
        val recipe = service.addStreetKitchenRecipe(info)
        return mapper.recipeModelToDto(recipe)
    }
}