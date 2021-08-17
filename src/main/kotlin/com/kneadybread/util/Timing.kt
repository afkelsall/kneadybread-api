package com.kneadybread.util

import java.time.Duration
import java.time.LocalDateTime

//suspend fun <T> withTimingSuspend(functionName: String, timingResults: MutableList<Pair<String, Duration>>, function: suspend () -> T): T {
//    val startTime = LocalDateTime.now()
//
//    val out = function()
//
//    val endTime = LocalDateTime.now()
//
//    val timeDifference = Duration.between(startTime, endTime)
//
//    println("TIMING: $functionName ${java.lang.Thread.currentThread()}: ${timeDifference.toMillis()}")
//
//    timingResults.add(Pair(functionName, timeDifference))
//
//    return out
//}

fun <T> withTiming(functionName: String, timingResults: MutableList<Pair<String, Duration>>? = null, function: () -> T): T {
    val startTime = LocalDateTime.now()

    val out = function()

    val endTime = LocalDateTime.now()

    val timeDifference = Duration.between(startTime, endTime)

    println("TIMING: $functionName ${java.lang.Thread.currentThread()}: ${timeDifference.toMillis()}")

    timingResults?.add(Pair(functionName, timeDifference))

    return out
}