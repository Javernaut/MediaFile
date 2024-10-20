package io.github.javernaut.mediafile

import android.graphics.Bitmap
import io.github.javernaut.mediafile.factory.MediaFileContext
import io.github.javernaut.mediafile.factory.NativeHandle
import io.github.javernaut.mediafile.factory.isValid


/**
 * Class for loading N equidistant frames from a video file.
 * Subject to change in future releases.
 */
class MediaFileFrameLoader internal constructor(
    private val nativeHandle: NativeHandle // MediaFileFrameLoader
) {
    // TODO Async?
    fun loadNextFrameInto(bitmap: Bitmap): Boolean {
        return nativeLoadFrame(nativeHandle, bitmap)
    }

    fun dispose() = dispose(nativeHandle)

    companion object {

        @JvmStatic
        private external fun dispose(frameLoaderHandle: NativeHandle) // MediaFileFrameLoader

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

        fun create(context: MediaFileContext, totalFramesToRead: Int): MediaFileFrameLoader? {
            return nativeCreateFrameLoader(context.nativeHandle, totalFramesToRead)
                .takeIf { it.isValid }
                ?.let { MediaFileFrameLoader(it) }
        }
    }
}
