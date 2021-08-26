package com.kneadybread.resources

import com.google.inject.Inject
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

class HealthResource @Inject constructor(
    application: Application
) {

    init {
        application.routing {
            get("/ping") {
                call.respond(HttpStatusCode.OK, "pong")
            }

            get("/healthcheck") {
                call.respond(HttpStatusCode.OK, "pong")
            }
        }
    }

}