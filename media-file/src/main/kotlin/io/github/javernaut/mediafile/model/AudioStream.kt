package io.github.javernaut.mediafile.model

class AudioStream(
    override val basicInfo: BasicStreamInfo,
    val bitRate: BitRate,
    val sampleFormat: String?,
    val sampleRate: SampleRate,
    val channels: Int,
    val channelLayout: String?
) : MediaStream

typealias SampleRate = Int
