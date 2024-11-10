package io.github.javernaut.mediafile.factory

import io.github.javernaut.mediafile.MediaFile
import io.github.javernaut.mediafile.creator.MediaType

// Owns AVFormatContext pointer
class MediaFileContext(
    private val nativeHandle: NativeHandle, // MediaFileContext
    private val mediaType: MediaType
) : AutoCloseable {

    private var closed = false

    private val subResources = mutableListOf<AutoCloseable>()

    @Synchronized
    fun readMetaData(): MediaFile? {
        if (closed) return null

        val builder = MediaFileBuilder()
        readMetaData(nativeHandle, builder, mediaType.mediaStreamsMask)
        return builder.create()
    }

    @Synchronized
    override fun close() {
        if (!closed) {
            subResources.forEach(AutoCloseable::close)
            dispose(nativeHandle)
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

    private external fun dispose(nativeHandle: NativeHandle) // MediaFileContext

    private external fun readMetaData(
        nativeHandle: NativeHandle, // MediaFileContext
        builder: MediaFileBuilder,
        mediaStreamsMask: Int
    )
}
