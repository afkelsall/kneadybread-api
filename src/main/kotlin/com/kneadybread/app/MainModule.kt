package com.kneadybread.app

import com.google.inject.AbstractModule
import com.kneadybread.resources.BakeResource
import com.kneadybread.resources.HealthResource
import io.ktor.application.*

class MainModule(private val application: Application): AbstractModule() {

    override fun configure() {
        bind(Application::class.java).toInstance(application)
        bind(BakeResource::class.java).asEagerSingleton()
        bind(HealthResource::class.java).asEagerSingleton()
    }

}
