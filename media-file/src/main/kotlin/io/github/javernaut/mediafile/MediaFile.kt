package io.github.javernaut.mediafile

import io.github.javernaut.mediafile.ext.FrameLoader
import io.github.javernaut.mediafile.model.MediaInfo

/**
 * Represents an opened media resource. An instance can be obtained from [MediaFileFactory] by supplying a [MediaSource].
 *
 * A MediaFile can provide a [MediaInfo] and a [FrameLoader] instances.
 *
 * The AutoClosable interface simplifies and denotes the importance of the instance disposal, as internally a pointer to an AVFormatContext is stored.
 */
class MediaFile internal constructor(
    private val nativeHandle: NativeHandle // MediaFile::Context
) : AutoCloseable {

    private var closed = false

    private val subResources = mutableListOf<AutoCloseable>()

    /**
     * Reads the meta info of the media resource. Returns null on IO error or if the MediaFile is closed.
     * Regardless of the types of the media resource, all the available audio, video and subtitle streams are read.
     */
    @Synchronized
    fun readMediaInfo(): MediaInfo? {
        if (closed) return null

        val builder = MediaInfoBuilder()
        nativeReadMediaInfo(nativeHandle, builder)
        return builder.create()
    }

    /**
     * Closing the instance and disposing the internal resources, including the child resources like [FrameLoader].
     */
    @Synchronized
    override fun close() {
        if (!closed) {
            subResources.forEach(AutoCloseable::close)
            nativeClose(nativeHandle)
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

    private external fun nativeClose(nativeHandle: NativeHandle) // MediaFile::Context

    private external fun nativeReadMediaInfo(
        nativeHandle: NativeHandle, // MediaFile::Context
        builder: MediaInfoBuilder
    )
}
