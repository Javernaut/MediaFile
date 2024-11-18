package io.github.javernaut.mediafile.model

import io.github.javernaut.mediafile.displayable.toDisplayable

/**
 * Represents metadata of an audio stream in a media file.
 */
data class AudioStream(
    override val basicInfo: BasicStreamInfo,

    /**
     * Bit rate.
     *
     * Can be converted to a human readable string using [BitRate.toDisplayable].
     */
    val bitRate: BitRate,

    /**
     * Sample format.
     */
    val sampleFormat: String?,

    /**
     * Sample rate.
     *
     * Can be converted to a human readable string using [SampleRate.toDisplayable].
     */
    val sampleRate: SampleRate,

    /**
     * Number of channels.
     */
    val channels: Int,

    /**
     * Channel layout.
     */
    val channelLayout: String?
) : MediaStream

@JvmInline
value class SampleRate(val value: Int)
