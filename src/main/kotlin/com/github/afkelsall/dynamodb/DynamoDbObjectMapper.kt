package com.github.afkelsall.dynamodb

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

class DynamoDbObjectMapper {

    fun getMapper(): ObjectMapper {
        return ObjectMapper()
            .registerKotlinModule()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .registerModule(JavaTimeModule())
            .registerModule(DynamoDbJacksonModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    }

}

