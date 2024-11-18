package io.github.javernaut.mediafile.model

import io.github.javernaut.mediafile.displayable.displayableLanguage
import io.github.javernaut.mediafile.displayable.getDisplayableDisposition

/**
 * Represents the common metadata of a media stream.
 */
data class BasicStreamInfo(
    /**
     * Index of the stream in the original media source.
     */
    val index: Int,

    /**
     * Optional title for the stream.
     */
    val title: String?,

    /**
     * Name of the codec used to encode the stream.
     */
    val codecName: String,

    /**
     * Language codes like 'eng'.
     *
     * Can be converted to a human readable string using [displayableLanguage].
     */
    val language: String?,

    /**
     * A bit mask of various flags, coming directly from FFmpeg.
     *
     * Can be converted to a human readable string using [getDisplayableDisposition].
     */
    val disposition: Int
)

@JvmInline
value class BitRate(val value: Long)
