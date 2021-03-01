package hu.balassa.recipe.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.Scope
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client


@Configuration
@Profile("develop")
class AwsS3StaticConfig {
    @Value("\${aws.secret.key}")
    private lateinit var awsSecretKey: String

    @Value("\${aws.accessKey}")
    private lateinit var awsAccessKey: String

    @Bean
    @Scope("prototype")
    fun awsS3Client(): S3Client = S3Client
                .builder()
                .region(Region.EU_CENTRAL_1)
                .credentialsProvider { AwsBasicCredentials.create(awsAccessKey, awsSecretKey) }
                .build()
}

@Configuration
@Profile("production")
class AwsS3InstanceRoleConfig {

    @Bean
    fun awsS3Client(): S3Client {
        return S3Client
            .builder()
            .region(Region.EU_CENTRAL_1)
            .build()
    }
}