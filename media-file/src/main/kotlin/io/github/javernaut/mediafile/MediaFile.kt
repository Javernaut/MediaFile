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
    private val nativeHandle: NativeHandle // MediaFileContext
) : AutoCloseable {

    private var closed = false

    private val subResources = mutableListOf<AutoCloseable>()

    /**
     * Reads the meta info about the media resource. Returns null on IO error or if the MediaFile is closed.
     * The [MediaType] parameter controls which types of streams should be read.
     */
    @Synchronized
    fun readMetaInfo(mediaType: MediaType): MediaInfo? {
        if (closed) return null

        val builder = MediaFileBuilder()
        nativeReadMetaInfo(nativeHandle, builder, mediaType.mediaStreamsMask)
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

    private external fun nativeClose(nativeHandle: NativeHandle) // MediaFileContext

    private external fun nativeReadMetaInfo(
        nativeHandle: NativeHandle, // MediaFileContext
        builder: MediaFileBuilder,
        mediaStreamsMask: Int
    )
}
