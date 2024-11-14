package io.github.javernaut.mediafile.factory

import android.content.Context
import io.github.javernaut.mediafile.MediaFile

object MediaFileFactory {
    init {
        System.loadLibrary("avutil")
        System.loadLibrary("avcodec")
        System.loadLibrary("avformat")
        System.loadLibrary("swscale")
        System.loadLibrary("media-file")
    }

    // TODO Describe the importance of calling this in order to use content:// URIs
    fun initWith(context: Context) = nativeInitWith(context.applicationContext)

    private external fun nativeInitWith(context: Context)

    fun create(source: MediaSource, type: MediaType): MediaFile? {
        return source.openContext()
            .takeIf { it.isValid }
            ?.let {
                MediaFile(it, type)
            }
    }
}
