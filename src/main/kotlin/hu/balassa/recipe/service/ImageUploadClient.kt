package hu.balassa.recipe.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.PutObjectRequest
import hu.balassa.recipe.util.Util
import org.springframework.stereotype.Service
import java.io.File
import java.io.InputStream
import java.net.URL
import java.nio.file.Files
import java.nio.file.StandardCopyOption


interface ImageUploadClient {
    fun uploadImageFromFile(image: File): String

    fun uploadImageFromImageURL(imageUrl: String): String {
        val stream: InputStream = URL(imageUrl).openStream()
        val path = Files.createTempFile(Util.generateUUID(), ".jpg")
        Files.copy(stream, path, StandardCopyOption.REPLACE_EXISTING)
        val file = path.toFile()

        val newUrl = uploadImageFromFile(file)

        file.deleteOnExit()
        return newUrl
    }
}

@Service
class AWSImageUploadClient(
    private val awsS3Client: AmazonS3
): ImageUploadClient {

    companion object {
        private const val bucket = "recipe-app-objects"
        private const val directory = "images"
    }

    override fun uploadImageFromFile(image: File): String =
            uploadImageToS3(bucket, directory, image.name, image)

    override fun uploadImageFromImageURL(imageUrl: String): String =
        try {
            super.uploadImageFromImageURL(imageUrl)
        } catch (err: Exception) {
            ""
        }

    private fun uploadImageToS3(bucket: String, directory: String, imageName: String, file: File): String {
        val request = PutObjectRequest(bucket, "$directory/$imageName", file)
        request.cannedAcl = CannedAccessControlList.PublicRead
        awsS3Client.putObject(request)
        val url = awsS3Client.getUrl("$bucket/$directory", imageName)
        return url.toExternalForm()
    }
}