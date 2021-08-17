package com.kneadybread.builder

import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import java.math.BigDecimal

class AttributeValueBuilder {

    companion object {
        fun string(value: String): AttributeValue {
            return AttributeValue.builder().s(value).build()
        }

        fun map(values: Map<String, AttributeValue>): AttributeValue {
            return AttributeValue.builder().m(values).build()
        }

        fun int(value: Int): AttributeValue {
            return AttributeValue.builder().n(value.toString()).build()
        }

        fun bigDecimal(value: BigDecimal): AttributeValue {
            return AttributeValue.builder().n(value.toString()).build()
        }

        fun binary(value: ByteArray): AttributeValue {
            return AttributeValue.builder().b(SdkBytes.fromByteArray(value)).build()
        }

        fun boolean(value: Boolean): AttributeValue {
            return AttributeValue.builder().bool(value).build()
        }

        fun nul(): AttributeValue {
            return AttributeValue.builder().nul(true).build()
        }

        fun list(value: List<AttributeValue>): AttributeValue {
            return AttributeValue.builder().l(value).build()
        }
    }

}
