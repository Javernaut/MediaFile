package io.github.javernaut.mediafile

import android.content.Context

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

        @JvmStatic
        fun setMinLogLevel(level: LogLevel) {
            nativeSetMinLogLevel(level.ordinal)
        }

        private var instance: MediaFileFactory? = null

        @Synchronized
        fun getDefault(context: Context): MediaFileFactory {
            return instance ?: MediaFileFactory().also {
                nativeInitWith(context.applicationContext)
                instance = it
            }
        }
    }

    fun create(source: MediaSource, type: MediaType): MediaFile? {
        return source.openContext()
            .takeIf { it.isValid }
            ?.let {
                MediaFile(it, type)
            }
    }
}
