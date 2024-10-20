package io.github.javernaut.mediafile.factory

// TODO Consider a value class instead and a type parameter
typealias NativeHandle = Long

val NativeHandle.isValid: Boolean
    get() = this != NO_REF

private const val NO_REF = 0L
