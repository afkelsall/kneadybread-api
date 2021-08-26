package com.kneadybread

import com.google.inject.Inject
import com.google.inject.Singleton
import io.ktor.application.*

@Singleton
class AppConfiguration @Inject constructor(private val application: Application) {

    val awsRegionString = application.environment.config.property("ktor.aws.region").getString()

    val dynamoDbAccessKeyId = application.environment.config.propertyOrNull("ktor.aws.dynamoDb.accessKeyId")?.getString()
    val dynamoDbAccessKey = application.environment.config.propertyOrNull("ktor.aws.dynamoDb.secretAccessKey")?.getString()
    val dynamoDbProfileName = application.environment.config.propertyOrNull("ktor.aws.dynamoDb.profileName")?.getString()

}