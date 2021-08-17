package com.kneadybread.app

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.afkelsall.dynamodb.DynamoDbJacksonModule
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton


class ObjectMapperModule: AbstractModule() {

    override fun configure() {}

    @Provides
    @Singleton
    private fun getMapper(): ObjectMapper {
        return ObjectMapper()
            .registerKotlinModule()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .registerModule(JavaTimeModule())
            .registerModule(DynamoDbJacksonModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    }
}


