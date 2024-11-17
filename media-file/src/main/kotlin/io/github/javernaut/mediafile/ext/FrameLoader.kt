package io.github.javernaut.mediafile.ext

import android.graphics.Bitmap
import io.github.javernaut.mediafile.MediaFile
import io.github.javernaut.mediafile.NativeHandle
import io.github.javernaut.mediafile.ext.FrameLoader.Companion.create
import io.github.javernaut.mediafile.isValid


/**
 * Class for loading N equidistant frames from a video file.
 * Subject to change in future releases.
 */
class FrameLoader internal constructor(
    private val nativeHandle: NativeHandle // MediaFileFrameLoader
) : AutoCloseable {

    private var closed = false

    @Synchronized
    fun loadNextFrameInto(bitmap: Bitmap): Boolean {
        return !closed && nativeLoadFrame(nativeHandle, bitmap)
    }

    @Synchronized
    override fun close() {
        if (!closed) {
            nativeClose(nativeHandle)
            closed = true
        }
    }

    companion object {

        @JvmStatic
        private external fun nativeClose(frameLoaderHandle: NativeHandle) // MediaFileFrameLoader

        @JvmStatic
        private external fun nativeLoadFrame(
            frameLoaderHandle: NativeHandle, // MediaFileFrameLoader
            bitmap: Bitmap
        ): Boolean

        @JvmStatic
        private external fun nativeCreateFrameLoader(
            contextHandle: NativeHandle, // MediaFileContext
            totalFramesToRead: Int
        ): NativeHandle // MediaFileFrameLoader

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

fun MediaFile.getFrameLoader(totalFramesToRead: Int): FrameLoader? {
    return create(this, totalFramesToRead)
}
