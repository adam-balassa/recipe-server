package hu.balassa.recipe.config

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope

@Configuration
class AwsConfig {
    @Value("\${aws.secret.key}")
    lateinit var awsSecretKey: String

    @Value("\${aws.accessKey}")
    lateinit var awsAccessKey: String

    @Bean
    @Scope("prototype")
    fun awsS3Client(): AmazonS3 {
        val credentials: AWSCredentials = BasicAWSCredentials(awsAccessKey, awsSecretKey)

        return AmazonS3ClientBuilder
            .standard()
            .withCredentials(AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.EU_CENTRAL_1)
            .build()
    }
}