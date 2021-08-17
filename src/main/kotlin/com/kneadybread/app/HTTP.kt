package com.kneadybread.plugins

import io.ktor.http.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.request.*

fun Application.configureHTTP() {
    install(CORS) {
        method(HttpMethod.Post)
        method(HttpMethod.Get)
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
//        header("MyCustomHeader")
        allowCredentials = true
        allowNonSimpleContentTypes = true
//        host("kneadybread.com", listOf("https"))
//        host("localhost:3000", listOf("http"))
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

}
