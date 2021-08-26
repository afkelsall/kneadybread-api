package com.kneadybread.service

import com.google.inject.Inject
import com.kneadybread.AppConfiguration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region

class AwsConfigSetupService @Inject constructor(private val config: AppConfiguration) {

    private class AwsConfigException(message: String): RuntimeException(message)

    fun getCredentialsProvider(): AwsCredentialsProvider {
        return if (config.dynamoDbAccessKeyId != null && config.dynamoDbAccessKey != null)
            StaticCredentialsProvider.create(AwsBasicCredentials.create(config.dynamoDbAccessKeyId, config.dynamoDbAccessKey))
        else if (config.dynamoDbProfileName != null)
            ProfileCredentialsProvider.builder().profileName(config.dynamoDbProfileName).build()
        else
            throw AwsConfigException("AWS DynamoDB credentials must be passed via enviornment variable, config file or AWS profile file.")
    }

    fun getAwsRegion(): Region {
        return Region.regions().firstOrNull() { it.id().equals(config.awsRegionString, true)} ?: throw AwsConfigException("Invalid AWS region specified: ${config.awsRegionString}.")
    }
}