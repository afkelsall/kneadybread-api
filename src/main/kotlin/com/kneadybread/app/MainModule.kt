package com.kneadybread.app

import com.github.afkelsall.dynamodb.DynamoDbAsycClientWrapper
import com.github.afkelsall.dynamodb.KeyInfo
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.kneadybread.resources.BakeResource
import com.kneadybread.resources.HealthResource
import io.ktor.application.*
import software.amazon.awssdk.http.async.SdkAsyncHttpClient
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient
import software.amazon.awssdk.regions.Region
import java.time.Duration

class MainModule(private val application: Application): AbstractModule() {

    override fun configure() {
        bind(Application::class.java).toInstance(application)
        bind(BakeResource::class.java).asEagerSingleton()
        bind(HealthResource::class.java).asEagerSingleton()
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
    fun get(): DynamoDbAsycClientWrapper<String, String> {
        return DynamoDbAsycClientWrapper(
            httpClientProvider(),
            Region.US_EAST_2,
            "kneadybread-dynamodb",
            "kneadybread",
            KeyInfo.PartitionKeyInfo.StringKey("bakerId"),
            KeyInfo.SortKeyInfo.StringKey("sortKey"),
        )
    }

}
