package io.github.javernaut.mediafile.factory

import io.github.javernaut.mediafile.MediaFile
import io.github.javernaut.mediafile.creator.MediaType

// Owns AVFormatContext pointer
class MediaFileContext(
    private val nativeHandle: Long,
    private val mediaType: MediaType
) {
    // Async?
    fun readMetaData(): MediaFile? {
        val builder = MediaFileBuilder()
        readMetaData(nativeHandle, builder, mediaType.mediaStreamsMask)
        return builder.create()
    }
//    fun getFrameReader(): FrameReader = TODO()

    fun dispose() = dispose(nativeHandle)

    private external fun dispose(nativeHandle: Long)

    private external fun readMetaData(
        nativeHandle: Long,
        builder: MediaFileBuilder,
        mediaStreamsMask: Int
    )
}
