package hu.balassa.recipe.service


import hu.balassa.recipe.util.Util
import org.apache.http.client.utils.URIBuilder
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.ObjectCannedACL
import software.amazon.awssdk.services.s3.model.PutObjectAclRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.File
import java.io.InputStream
import java.net.URI
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
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
    private val s3: S3Client
): ImageUploadClient {

    companion object {
        private const val bucket = "recipe-app-objects"
        private const val directory = "images"
    }

    override fun uploadImageFromFile(image: File): String {
        uploadImageToS3(bucket, directory, image)
        return "https://$bucket.s3.eu-central-1.amazonaws.com/$directory/${image.name}"
    }

    override fun uploadImageFromImageURL(imageUrl: String): String =
        try {
            super.uploadImageFromImageURL(imageUrl)
        } catch (err: Exception) {
            err.printStackTrace()
            ""
        }

    private fun uploadImageToS3(bucket: String, directory: String, file: File) {
        print(file.name)
        val request = PutObjectRequest
            .builder()
            .bucket(bucket)
            .acl(ObjectCannedACL.PUBLIC_READ)
            .key("$directory/${file.name}")
            .build()

        s3.putObject(request, Path.of(file.toURI()))
    }
}