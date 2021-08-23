package com.github.afkelsall.dynamodb

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import com.fasterxml.jackson.databind.module.SimpleDeserializers
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.module.SimpleSerializers
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import com.kneadybread.builder.AttributeValueBuilder
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import java.io.IOException
import java.math.BigDecimal

/**
 *  Class that registers a serializer and deserializer for the DynamoDB AttributeValue class.
 *
 * <pre>
 * ObjectMapper mapper = new ObjectMapper();
 * mapper.registerModule(new DynamoDbJacksonModule());
 * </pre>
 *
 *
 */
class DynamoDbJacksonModule: SimpleModule() {

    override fun setupModule(context: SetupContext) {
        super.setupModule(context)

        val serializer = SimpleSerializers()
        serializer.addSerializer(AttributeValue::class.java, AttributeValueSerializer())

        val deserializer = SimpleDeserializers()
        deserializer.addDeserializer(AttributeValue::class.java, AttributeValueDeserializer())

        context.addSerializers(serializer)
        context.addDeserializers(deserializer)
    }


    // Json => AttributeValue
    private class AttributeValueDeserializer: StdDeserializer<AttributeValue>(AttributeValue::class.java) {


        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): AttributeValue {
            if (p.currentToken() == JsonToken.START_OBJECT) {
                p.nextToken()
                val out = p.readValueAs<Map<String,AttributeValue>>(jacksonTypeRef<Map<String,AttributeValue>>())
                return AttributeValueBuilder.map(out)
            }

            if (p.currentToken() == JsonToken.START_ARRAY) {
                val out = p.readValueAs<List<AttributeValue>>(jacksonTypeRef<List<AttributeValue>>())
                return AttributeValueBuilder.list(out)
            }

            val node = p.codec.readTree<JsonNode>(p)

            return when {
                node.isTextual -> AttributeValueBuilder.string(node.textValue())
                node.isInt -> AttributeValueBuilder.int(node.asInt())
                node.isNumber -> AttributeValueBuilder.bigDecimal(node.decimalValue())
                node.isBinary -> AttributeValueBuilder.binary(node.binaryValue())
                node.isBoolean -> AttributeValueBuilder.boolean(node.asBoolean())
                node.isNull -> AttributeValueBuilder.nul()
                else -> throw Exception("Unhandled AttributeValue type")
            }
        }

    }

    // List<AttributeValue> => Json
    private class AttributeValueSerializer: StdSerializer<AttributeValue>(AttributeValue::class.java) {
        @Throws(IOException::class, JsonProcessingException::class)

        override fun serializeWithType(
            value: AttributeValue?,
            gen: JsonGenerator?,
            serializers: SerializerProvider?,
            typeSer: TypeSerializer?
        ) {
            this.serialize(value, gen, serializers)
        }

        override fun serialize(value: AttributeValue?, jgen: JsonGenerator?, provider: SerializerProvider?) {
            if (jgen == null) return

            if (value == null) {
                jgen.writeNull()
                return
            }

            when {
                value.bool() != null -> jgen.writeBoolean(value.bool())
                value.s() != null -> jgen.writeString(value.s())
                value.n() != null -> jgen.writeNumber(BigDecimal(value.n()))
                value.b() != null -> jgen.writeBinary(value.b().asByteArray())
                value.hasSs() -> provider?.defaultSerializeValue(value.ss(), jgen)
                value.hasNs() -> provider?.defaultSerializeValue(value.ns(), jgen)
                value.hasBs() -> provider?.defaultSerializeValue(value.bs(), jgen)
                value.hasL() -> provider?.defaultSerializeValue(value.l(), jgen)
                value.hasM() -> provider?.defaultSerializeValue(value.m(), jgen)
                value.nul() != null && value.nul() -> jgen.writeNull()
            }
        }
    }

}