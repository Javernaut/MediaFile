package io.github.javernaut.mediafile

import android.graphics.Bitmap


/**
 * Class for loading exactly 4 equidistant frames from a video file.
 * Subject to change in future releases.
 */
class FrameLoader internal constructor(private var frameLoadingContextHandle: Long) {

    private var framesDecoded = 0

    fun loadNextFrameInto(bitmap: Bitmap): Boolean {
        val result = nativeLoadFrame(frameLoadingContextHandle, framesDecoded, bitmap)
        framesDecoded++
        return result
    }

    fun release() {
        nativeRelease(frameLoadingContextHandle)
        frameLoadingContextHandle = -1
    }

    companion object {
        const val TOTAL_FRAMES_TO_LOAD = 4

        @JvmStatic
        private external fun nativeRelease(handle: Long)

        @JvmStatic
        private external fun nativeLoadFrame(handle: Long, index: Int, bitmap: Bitmap): Boolean
    }
}
