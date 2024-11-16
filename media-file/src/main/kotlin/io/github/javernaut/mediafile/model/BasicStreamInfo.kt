package io.github.javernaut.mediafile.model

class BasicStreamInfo(
    val index: Int,
    val title: String?,
    val codecName: String,
    val language: String?,
    val disposition: Int
)

typealias BitRate = Long
