package io.github.javernaut.mediafile.factory

import io.github.javernaut.mediafile.MediaFile
import io.github.javernaut.mediafile.creator.MediaType

// Owns AVFormatContext pointer
class MediaFileContext(
    internal val nativeHandle: NativeHandle, // MediaFileContext
    private val mediaType: MediaType
) {
    // Async?
    fun readMetaData(): MediaFile? {
        val builder = MediaFileBuilder()
        readMetaData(nativeHandle, builder, mediaType.mediaStreamsMask)
        return builder.create()
    }

    fun dispose() = dispose(nativeHandle)

    private external fun dispose(nativeHandle: NativeHandle) // MediaFileContext

    private external fun readMetaData(
        nativeHandle: NativeHandle, // MediaFileContext
        builder: MediaFileBuilder,
        mediaStreamsMask: Int
    )
}
