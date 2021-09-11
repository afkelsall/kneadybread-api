package com.kneadybread.resources

import com.google.inject.Inject
import com.kneadybread.domain.request.BakeDetailsRequest
import com.kneadybread.domain.request.BakeRequest
import com.kneadybread.service.BakeService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.locations.put
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

            get<NewBakeLocation> { bakeUser ->
                val response = bakeService.getBakeList(bakeUser.user)

                call.respond(HttpStatusCode.OK, response)
            }

            post<NewBakeLocation> { bakeUser ->
                val request = call.receive<BakeDetailsRequest>()

                bakeService.saveNewBake(bakeUser.user, request)
                val response = bakeService.getBakeList(bakeUser.user)

                call.respond(HttpStatusCode.OK, response)
            }


            @Location("/user/{user}/bake/{bake}")
            data class BakeLocation(val user: String, val bake: String)

            get<BakeLocation> { bakeLocation ->
                logger.info { "incoming" }
                val response = bakeService.getBake(bakeLocation.user, bakeLocation.bake)

                call.respond(HttpStatusCode.OK, response)
            }

            put<BakeLocation> { bakeLocation ->
                val request = call.receive<BakeDetailsRequest>()

                val response = bakeService.saveBake(bakeLocation.user, request)

                call.respond(HttpStatusCode.OK, response)
            }

            post<BakeLocation> { bakeLocation ->

                try {
                    val request = call.receive<BakeRequest>()

                    val response = bakeService.updateBake(bakeLocation.user, bakeLocation.bake, request)

                    call.respond(HttpStatusCode.OK, response)
                } catch (e: Exception) {
                    logger.error({ "Errored" }, e)
                }
            }

            delete<BakeLocation> { bakeLocation ->
//                val request = call.receive<BakeRequest>()

                bakeService.deleteBake(bakeLocation.user, bakeLocation.bake)

                call.respond(HttpStatusCode.OK)
            }
        }
    }
}

