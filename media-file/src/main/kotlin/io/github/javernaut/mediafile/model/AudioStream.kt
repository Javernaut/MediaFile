package io.github.javernaut.mediafile.model

/**
 * Represents metadata of an audio stream in a media file.
 */
data class AudioStream(
    override val basicInfo: BasicStreamInfo,

    /**
     * Bit rate.
     */
    val bitRate: BitRate,

    /**
     * Sample format.
     */
    val sampleFormat: String?,

    /**
     * Sample rate.
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

typealias SampleRate = Int
