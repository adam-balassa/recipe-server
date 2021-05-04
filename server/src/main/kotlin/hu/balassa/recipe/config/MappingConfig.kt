package hu.balassa.recipe.config

import hu.balassa.recipe.service.mapping.DtoModelMapper
import org.mapstruct.factory.Mappers
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class MappingConfig {
    @Bean
    open fun mapper(): DtoModelMapper =
        Mappers.getMapper(DtoModelMapper::class.java)
}