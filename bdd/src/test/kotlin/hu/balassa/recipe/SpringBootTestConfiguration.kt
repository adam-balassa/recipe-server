package hu.balassa.recipe

import hu.balassa.recipe.SpringBootTestConfiguration.WebTestClientConfiguration
import io.cucumber.java.Before
import io.cucumber.spring.CucumberContextConfiguration
import org.mockito.Mockito.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.test.web.reactive.server.WebTestClient
import software.amazon.awssdk.services.s3.S3Client


@SpringBootTest(classes = [RecipeApplication::class, WebTestClientConfiguration::class], webEnvironment = DEFINED_PORT)
@CucumberContextConfiguration
@AutoConfigureWebTestClient
class SpringBootTestConfiguration {
    @TestConfiguration
    class WebTestClientConfiguration {
        @Autowired
        lateinit var context: ApplicationContext

        @Bean
        fun webTestClient(): WebTestClient =
            WebTestClient.bindToServer().baseUrl("http://localhost:8080").build()

        @Bean
        @Primary
        fun s3Client(): S3Client {
            return mock(S3Client::class.java)
        }
    }

    @Before
    fun placeHolder() {}
}