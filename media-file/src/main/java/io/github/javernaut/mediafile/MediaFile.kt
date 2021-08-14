package io.github.javernaut.mediafile

import android.os.ParcelFileDescriptor

/**
 * A structure that has metadata of a video or audio file and its media streams.
 */
class MediaFile internal constructor(
    val fileFormatName: String,
    val videoStream: VideoStream?,
    val audioStreams: List<AudioStream>,
    val subtitleStreams: List<SubtitleStream>,
    private val parcelFileDescriptor: ParcelFileDescriptor?,
    frameLoaderContextHandle: Long?
) {

    /**
     * true if file is accessed by file: protocol and false if by pipe:
     */
    val fullFeatured = parcelFileDescriptor == null

    var frameLoader = frameLoaderContextHandle?.let { FrameLoader(frameLoaderContextHandle) }
        private set

    fun supportsFrameLoading() = videoStream != null && frameLoader != null

    fun release() {
        frameLoader?.release()
        frameLoader = null
        parcelFileDescriptor?.close()
    }
}
