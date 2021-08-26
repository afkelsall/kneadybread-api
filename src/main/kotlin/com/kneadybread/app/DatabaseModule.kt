package com.kneadybread.app

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.afkelsall.dynamodb.DynamoDbAsycClientWrapper
import com.github.afkelsall.dynamodb.KeyInfo
import com.google.inject.AbstractModule
import com.google.inject.Inject
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import com.kneadybread.service.AwsConfigSetupService
import software.amazon.awssdk.http.async.SdkAsyncHttpClient
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient
import java.time.Duration

class DatabaseModule(): AbstractModule() {

    override fun configure() {
    }

    @Provides
    @Singleton
    fun httpClientProvider(): SdkAsyncHttpClient {
        return NettyNioAsyncHttpClient.builder()
            .maxConcurrency(100)
            .maxPendingConnectionAcquires(10000)
            .connectionMaxIdleTime(Duration.ofHours(1))
//            .connectionTimeToLive(Duration.ofSeconds(60))
            .useIdleConnectionReaper(true)
            .build()
    }

    @Provides
    @Singleton
    @Inject
    @Named("kneadybread")
    fun get(featuredMapper: ObjectMapper, awsConfigService: AwsConfigSetupService): DynamoDbAsycClientWrapper<String, String> {


        return DynamoDbAsycClientWrapper(
            httpClientProvider(),
            awsConfigService.getAwsRegion(),
            awsConfigService.getCredentialsProvider(),
            "kneadybread",
            KeyInfo.PartitionKeyInfo.StringKey("bakerId"),
            KeyInfo.SortKeyInfo.StringKey("sortKey"),
            featuredMapper
        )
    }

}
