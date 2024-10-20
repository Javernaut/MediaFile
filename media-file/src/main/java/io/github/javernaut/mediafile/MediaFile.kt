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
    @Deprecated("Migrate to FrameLoader from MediaFileContext")
    private val parcelFileDescriptor: ParcelFileDescriptor?,
    frameLoaderContextHandle: Long?
) {

    /**
     * true if file is accessed by file: protocol and false if by pipe:
     */
    val fullFeatured = parcelFileDescriptor == null

    var legacyFrameLoader =
        frameLoaderContextHandle?.let { LegacyFrameLoader(frameLoaderContextHandle) }
        private set

    fun supportsFrameLoading() = videoStream != null && legacyFrameLoader != null

    fun release() {
        legacyFrameLoader?.release()
        legacyFrameLoader = null
        parcelFileDescriptor?.close()
    }
}
