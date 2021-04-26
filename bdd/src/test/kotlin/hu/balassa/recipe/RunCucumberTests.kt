package hu.balassa.recipe

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.runner.RunWith
import java.io.File

@RunWith(Cucumber::class)
@CucumberOptions(features = ["src/test/resources/features"], plugin = ["html:target/cucumber.html", "pretty"])
class RunCucumberTests