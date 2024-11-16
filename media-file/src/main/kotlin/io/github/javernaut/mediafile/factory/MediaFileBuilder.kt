package io.github.javernaut.mediafile.factory

import io.github.javernaut.mediafile.model.AudioStream
import io.github.javernaut.mediafile.model.BasicStreamInfo
import io.github.javernaut.mediafile.model.Container
import io.github.javernaut.mediafile.model.MediaInfo
import io.github.javernaut.mediafile.model.SubtitleStream
import io.github.javernaut.mediafile.model.VideoStream

/**
 * Class that aggregates a creation process of a [MediaInfo] object. Certain private methods are
 * called from JNI layer.
 */
internal class MediaFileBuilder {

    private var error = false

    private var container: Container? = null
    private var videoStream: VideoStream? = null
    private var audioStreams = mutableListOf<AudioStream>()
    private var subtitleStream = mutableListOf<SubtitleStream>()

    /**
     * Combines all data read from FFmpeg into a [MediaInfo] object. If there was error during
     * metadata reading then null is returned.
     */
    fun create(): MediaInfo? {
        if (error) return null

        return MediaInfo(
            container!!,
            videoStream,
            audioStreams,
            subtitleStream
        )
    }

    /* Used from JNI */
    @SuppressWarnings("UnusedPrivateMember")
    private fun onError() {
        this.error = true
    }

    /* Used from JNI */
    @SuppressWarnings("UnusedPrivateMember")
    private fun onMediaFileFound(formatName: String) {
        container = Container(formatName)
    }

    /* Used from JNI */
    @SuppressWarnings("UnusedPrivateMember")
    private fun onVideoStreamFound(
        basicStreamInfo: BasicStreamInfo,
        bitRate: Long,
        frameRate: Double,
        frameWidth: Int,
        frameHeight: Int
    ) {
        if (videoStream == null) {
            videoStream = VideoStream(
                basicStreamInfo,
                bitRate,
                frameRate,
                frameWidth,
                frameHeight
            )
        }
    }

    /* Used from JNI */
    @SuppressWarnings("UnusedPrivateMember")
    private fun onAudioStreamFound(
        basicStreamInfo: BasicStreamInfo,
        bitRate: Long,
        sampleFormat: String?,
        sampleRate: Int,
        channels: Int,
        channelLayout: String?
    ) {
        audioStreams.add(
            AudioStream(basicStreamInfo, bitRate, sampleFormat, sampleRate, channels, channelLayout)
        )
    }

    /* Used from JNI */
    @SuppressWarnings("UnusedPrivateMember")
    private fun onSubtitleStreamFound(basicStreamInfo: BasicStreamInfo) {
        subtitleStream.add(
            SubtitleStream(basicStreamInfo)
        )
    }

    /* Used from JNI */
    @SuppressWarnings("UnusedPrivateMember")
    private fun createBasicInfo(
        index: Int,
        title: String?,
        codecName: String,
        language: String?,
        disposition: Int
    ) =
        BasicStreamInfo(index, title, codecName, language, disposition)
}
