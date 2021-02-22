package hu.balassa.recipe.service

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.model.SetObjectAclRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import util.Util
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
class AWSImageUploadClient: ImageUploadClient {
    @Value("\${aws.secretKey}")
    lateinit var awsSecretKey: String

    @Value("\${aws.accessKey}")
    lateinit var awsAccessKey: String

    companion object {
        private const val bucket = "recipe-app-objects"
        private const val directory = "images"
    }

    override fun uploadImageFromFile(image: File): String =
            uploadImageToS3(bucket, directory, image.name, image)

    private fun uploadImageToS3(bucket: String, directory: String, imageName: String, file: File): String {
        val aws = getAWSClient()
        val request = PutObjectRequest(bucket, "$directory/$imageName", file)
        request.cannedAcl = CannedAccessControlList.PublicRead
        aws.putObject(request)
        val url = aws.getUrl("$bucket/$directory", imageName)
        return url.toExternalForm()
    }

    private fun getAWSClient(): AmazonS3 {
        val credentials: AWSCredentials = BasicAWSCredentials(awsAccessKey, awsSecretKey)

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_CENTRAL_1)
                .build()
    }
}