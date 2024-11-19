package io.github.javernaut.mediafile.ext

import android.graphics.Bitmap
import io.github.javernaut.mediafile.MediaFile
import io.github.javernaut.mediafile.NativeHandle
import io.github.javernaut.mediafile.ext.FrameLoader.Companion.create
import io.github.javernaut.mediafile.isValid


/**
 * Class for loading N equidistant frames from a video stream of a [MediaFile].
 * Subject to change in future releases.
 */
class FrameLoader internal constructor(
    private val nativeHandle: NativeHandle // MediaFile::FrameLoader
) : AutoCloseable {

    private var closed = false

    /**
     * Loads the next frame into [bitmap]. The management of Bitmap objects is up to the client.
     * The metrics of the incoming [bitmap] are respected and the original frame will be scaled to fit the bitmap.
     */
    @Synchronized
    fun loadNextFrameInto(bitmap: Bitmap): Boolean {
        return !closed && nativeLoadFrame(nativeHandle, bitmap)
    }

    /**
     * Closing the instance and disposing the internal resources.
     */
    @Synchronized
    override fun close() {
        if (!closed) {
            nativeClose(nativeHandle)
            closed = true
        }
    }

    companion object {

        @JvmStatic
        private external fun nativeClose(frameLoaderHandle: NativeHandle) // MediaFile::FrameLoader

        @JvmStatic
        private external fun nativeLoadFrame(
            frameLoaderHandle: NativeHandle, // MediaFile::FrameLoader
            bitmap: Bitmap
        ): Boolean

        @JvmStatic
        private external fun nativeCreateFrameLoader(
            contextHandle: NativeHandle, // MediaFile::Context
            totalFramesToRead: Int
        ): NativeHandle // MediaFile::FrameLoader

        internal fun create(
            context: MediaFile,
            totalFramesToRead: Int
        ): FrameLoader? {
            return context.produceSubResource { contextHandle ->
                nativeCreateFrameLoader(contextHandle, totalFramesToRead)
                    .takeIf { it.isValid }
                    ?.let { FrameLoader(it) }
            }
        }
    }
}

/**
 * Creates a [FrameLoader] instance for the [MediaFile], which will produce [totalFramesToRead] equidistant frames.
 */
fun MediaFile.getFrameLoader(totalFramesToRead: Int): FrameLoader? {
    return create(this, totalFramesToRead)
}
