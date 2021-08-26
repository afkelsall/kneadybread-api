package com.kneadybread

import com.google.inject.Guice
import com.kneadybread.app.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.locations.*
import io.ktor.server.netty.*
import org.slf4j.event.Level

fun main (args: Array<String>) {
    EngineMain.main(args)
}

fun Application.main() {
    configureHTTP()
    configureSerialization()

    install(Locations)

    install(CallLogging) {
        level = Level.INFO
    }

    Guice.createInjector(
        MainModule(this),
        ObjectMapperModule(),
        DatabaseModule()
    )
}
