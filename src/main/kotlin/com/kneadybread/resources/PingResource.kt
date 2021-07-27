package com.kneadybread.resources

import com.google.inject.Inject
import com.kneadybread.service.PingService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import java.lang.Appendable

class PingResource @Inject constructor(application: Application, pingService: PingService) {

    init {
        application.routing {
            get("/ping") {
                call.respond(HttpStatusCode.OK, "pong")
            }
        }
    }

}