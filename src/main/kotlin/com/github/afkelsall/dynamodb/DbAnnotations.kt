package com.github.afkelsall.dynamodb

@Target(AnnotationTarget.PROPERTY)
annotation class PartitionKey(val attributeName: String = "")

@Target(AnnotationTarget.PROPERTY)
annotation class SortKey(val attributeName: String = "")

@Target(AnnotationTarget.PROPERTY)
annotation class Property(val attributeName: String = "")

@Target(AnnotationTarget.CLASS)
annotation class DbObject