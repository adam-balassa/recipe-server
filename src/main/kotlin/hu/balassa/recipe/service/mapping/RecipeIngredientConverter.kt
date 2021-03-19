package hu.balassa.recipe.service.mapping

import com.fasterxml.jackson.databind.ObjectMapper
import hu.balassa.recipe.model.IngredientGroup
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

class RecipeIngredientConverter: AttributeConverter<List<IngredientGroup>> {
    override fun transformFrom(ig: List<IngredientGroup>): AttributeValue {
        val jsonString = ObjectMapper().writeValueAsString(ig)
        return AttributeValue.builder().s(jsonString).build()
    }

    override fun transformTo(attr: AttributeValue): List<IngredientGroup> {
        return ObjectMapper().readValue(attr.s(), Array<IngredientGroup>::class.java).toList()
    }

    override fun type(): EnhancedType<List<IngredientGroup>> {
        return EnhancedType.listOf(IngredientGroup::class.java)
    }

    override fun attributeValueType(): AttributeValueType {
        return AttributeValueType.S
    }
}