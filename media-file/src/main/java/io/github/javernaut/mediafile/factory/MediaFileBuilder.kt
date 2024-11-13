package io.github.javernaut.mediafile.factory

import androidx.annotation.Keep
import io.github.javernaut.mediafile.model.AudioStream
import io.github.javernaut.mediafile.model.BasicStreamInfo
import io.github.javernaut.mediafile.model.MediaFile
import io.github.javernaut.mediafile.model.SubtitleStream
import io.github.javernaut.mediafile.model.VideoStream

/**
 * Class that aggregates a creation process of a [MediaFile] object. Certain private methods are
 * called from JNI layer.
 */
internal class MediaFileBuilder {

    private var error = false

    // TODO encapsulate the container info somehow, potentially with more data (like the protocol)
    private var fileFormatName: String? = null

    private var videoStream: VideoStream? = null
    private var audioStreams = mutableListOf<AudioStream>()
    private var subtitleStream = mutableListOf<SubtitleStream>()

    /**
     * Combines all data read from FFmpeg into a [MediaFile] object. If there was error during
     * metadata reading then null is returned.
     */
    fun create(): MediaFile? {
        return if (!error) {
            MediaFile(
                fileFormatName!!,
                videoStream,
                audioStreams,
                subtitleStream
            )
        } else {
            null
        }
    }

    /* Used from JNI */
    @Keep
    @SuppressWarnings("UnusedPrivateMember")
    private fun onError() {
        this.error = true
    }

    /* Used from JNI */
    @Keep
    @SuppressWarnings("UnusedPrivateMember")
    private fun onMediaFileFound(fileFormatName: String) {
        this.fileFormatName = fileFormatName
    }

    /* Used from JNI */
    @Keep
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
    @Keep
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
    @Keep
    @SuppressWarnings("UnusedPrivateMember")
    private fun onSubtitleStreamFound(basicStreamInfo: BasicStreamInfo) {
        subtitleStream.add(
            SubtitleStream(basicStreamInfo)
        )
    }

    /* Used from JNI */
    @Keep
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
