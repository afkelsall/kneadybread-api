package com.kneadybread

import com.google.inject.Guice
import com.kneadybread.module.MainModule
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.kneadybread.plugins.*
import io.ktor.application.*

fun main (args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.main() {
    configureHTTP()
    configureRouting()
    configureSerialization()

    Guice.createInjector(MainModule(this))
}
