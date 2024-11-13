package io.github.javernaut.mediafile.model

import androidx.annotation.Keep

@Keep
class BasicStreamInfo internal constructor(
    val index: Int,
    val title: String?,
    val codecName: String,
    val language: String?,
    val disposition: Int
)

typealias BitRate = Long
