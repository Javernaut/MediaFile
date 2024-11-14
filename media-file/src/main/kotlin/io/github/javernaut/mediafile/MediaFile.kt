package io.github.javernaut.mediafile

import io.github.javernaut.mediafile.creator.MediaType
import io.github.javernaut.mediafile.factory.MediaFileBuilder
import io.github.javernaut.mediafile.factory.NativeHandle
import io.github.javernaut.mediafile.model.MediaInfo

// Owns AVFormatContext pointer
class MediaFile internal constructor(
    private val nativeHandle: NativeHandle, // MediaFileContext
    private val mediaType: MediaType
) : AutoCloseable {

    private var closed = false

    private val subResources = mutableListOf<AutoCloseable>()

    @Synchronized
    fun readMetaData(): MediaInfo? {
        if (closed) return null

        val builder = MediaFileBuilder()
        readMetaData(nativeHandle, builder, mediaType.mediaStreamsMask)
        return builder.create()
    }

    @Synchronized
    override fun close() {
        if (!closed) {
            subResources.forEach(AutoCloseable::close)
            close(nativeHandle)
            closed = true
        }
    }

    @Synchronized
    private fun register(subResource: AutoCloseable) {
        if (!closed) {
            subResources.add(subResource)
        }
    }

    @Synchronized
    internal fun <T : AutoCloseable> produceSubResource(producer: (NativeHandle) -> T?): T? {
        if (closed) return null

        return producer(nativeHandle)
            ?.also { register(it) }
    }

    private external fun close(nativeHandle: NativeHandle) // MediaFileContext

    private external fun readMetaData(
        nativeHandle: NativeHandle, // MediaFileContext
        builder: MediaFileBuilder,
        mediaStreamsMask: Int
    )
}
