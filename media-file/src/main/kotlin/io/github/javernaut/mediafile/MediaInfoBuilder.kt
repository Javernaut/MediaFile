package io.github.javernaut.mediafile

import android.util.Size
import io.github.javernaut.mediafile.model.AudioStream
import io.github.javernaut.mediafile.model.BasicStreamInfo
import io.github.javernaut.mediafile.model.BitRate
import io.github.javernaut.mediafile.model.Container
import io.github.javernaut.mediafile.model.FrameRate
import io.github.javernaut.mediafile.model.MediaInfo
import io.github.javernaut.mediafile.model.SampleRate
import io.github.javernaut.mediafile.model.SubtitleStream
import io.github.javernaut.mediafile.model.VideoStream

/**
 * Class that aggregates the creation process of a [MediaInfo] object. Certain private methods are
 * called from the JNI layer.
 */
internal class MediaInfoBuilder {

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
                BitRate(bitRate),
                FrameRate(frameRate),
                Size(frameWidth, frameHeight)
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
            AudioStream(
                basicStreamInfo,
                BitRate(bitRate),
                sampleFormat,
                SampleRate(sampleRate),
                channels,
                channelLayout
            )
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
