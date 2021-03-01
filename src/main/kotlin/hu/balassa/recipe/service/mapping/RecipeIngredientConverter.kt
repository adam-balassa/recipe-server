package hu.balassa.recipe.service.mapping

import com.fasterxml.jackson.databind.ObjectMapper
import hu.balassa.recipe.model.IngredientGroup
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

class RecipeIngredientConverter: AttributeConverter<Set<IngredientGroup>> {
    override fun transformFrom(ig: Set<IngredientGroup>): AttributeValue {
        val jsonString = ObjectMapper().writeValueAsString(ig)
        return AttributeValue.builder().s(jsonString).build()
    }

    override fun transformTo(attr: AttributeValue): Set<IngredientGroup> {
        return ObjectMapper().readValue(attr.s(), Array<IngredientGroup>::class.java).toSet()
    }

    override fun type(): EnhancedType<Set<IngredientGroup>> {
        return EnhancedType.setOf(IngredientGroup::class.java)
    }

    override fun attributeValueType(): AttributeValueType {
        return AttributeValueType.S
    }
}