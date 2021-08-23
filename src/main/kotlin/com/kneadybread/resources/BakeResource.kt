package com.kneadybread.resources

import com.google.inject.Inject
import com.kneadybread.domain.request.NewBakeRequest
import com.kneadybread.service.BakeService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import software.amazon.awssdk.utils.Logger


class BakeResource @Inject constructor(application: Application, val bakeService: BakeService) {

    companion object {
        private val logger = Logger.loggerFor(BakeResource::class.java)
    }

    init {
        application.routing {
            @Location("/user/{user}/bake")
            data class NewBakeLocation(val user: String)

            post<NewBakeLocation> { bakeUser ->
                val request = call.receive<NewBakeRequest>()
                bakeService.saveNewBake(bakeUser.user, request)
                val response = bakeService.getBakeList(bakeUser.user)
                logger.info{ "Post bake: Got response ready" }
                call.respond(HttpStatusCode.OK, response)
            }

            get<NewBakeLocation> { bakeUser ->
                val response = bakeService.getBakeList(bakeUser.user)
                logger.info{ "Get bakes: Got response ready" }
                call.respond(HttpStatusCode.OK, response)
            }
        }
    }
}

