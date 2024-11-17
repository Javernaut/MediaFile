package io.github.javernaut.mediafile

import android.content.Context

/**
 * MediaFileFactory class represents the entry point for [MediaFile] instances retrieval.
 */
class MediaFileFactory private constructor() {
    companion object {
        init {
            System.loadLibrary("avutil")
            System.loadLibrary("avcodec")
            System.loadLibrary("avformat")
            System.loadLibrary("swscale")
            System.loadLibrary("media-file")
        }

        @JvmStatic
        private external fun nativeInitWith(context: Context)

        @JvmStatic
        private external fun nativeSetMinLogLevel(level: Int)

        /**
         * Sets the minimum logging level for FFmpeg's logs. The default is [LogLevel.QUITE].
         */
        @JvmStatic
        fun setMinLogLevel(level: LogLevel) {
            nativeSetMinLogLevel(level.ordinal)
        }

        private var instance: MediaFileFactory? = null

        /**
         * Returns [MediaFileFactory] instance (effectively making it a singleton) and makes sure the native part is initialized only once.
         */
        @Synchronized
        fun getDefault(context: Context): MediaFileFactory {
            return instance ?: MediaFileFactory().also {
                nativeInitWith(context.applicationContext)
                instance = it
            }
        }
    }

    /**
     * Creates a [MediaFile] instance from the given [MediaSource].
     */
    fun create(source: MediaSource): MediaFile? {
        return source.openContext()
            .takeIf { it.isValid }
            ?.let {
                MediaFile(it)
            }
    }
}
