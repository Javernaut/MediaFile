package io.github.javernaut.mediafile

// TODO Consider a value class instead and a type parameter
internal typealias NativeHandle = Long

internal val NativeHandle.isValid: Boolean
    get() = this != NO_REF

private const val NO_REF = 0L
