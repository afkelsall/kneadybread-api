package com.kneadybread.module

import com.google.inject.AbstractModule
import com.kneadybread.resources.PingResource
import io.ktor.application.*

class MainModule(private val application: Application): AbstractModule() {

    override fun configure() {
        bind(PingResource::class.java).asEagerSingleton()

        bind(Application::class.java).toInstance(application)
    }

}
