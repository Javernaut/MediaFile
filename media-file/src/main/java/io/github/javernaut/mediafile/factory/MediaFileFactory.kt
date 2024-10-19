package io.github.javernaut.mediafile.factory

import android.content.Context
import android.net.Uri
import io.github.javernaut.mediafile.creator.MediaType

object MediaFileFactory {
    init {
        // The order of importing is mandatory, because otherwise the app will crash on Android API 16 and 17.
        // See: https://android.googlesource.com/platform/bionic/+/master/android-changes-for-ndk-developers.md#changes-to-library-dependency-resolution
        System.loadLibrary("avutil")
        System.loadLibrary("avcodec")
        System.loadLibrary("avformat")
        System.loadLibrary("swscale")
        System.loadLibrary("media-file")
    }

    fun initWith(context: Context) = nativeInitWith(context.applicationContext)

    private external fun nativeInitWith(context: Context)

    fun create(request: Request, type: MediaType): MediaFileContext? {
        return openContext(request.url)
            .takeIf { it != Reinterpret.NO_REF }
            ?.let(::MediaFileContext)
    }

    private external fun openContext(url: String): Long
}

object Reinterpret {
    const val NO_REF = 0L
}

// Owns AVFormatContext pointer
class MediaFileContext(
    private val nativeHandle: Long
) {
    fun readMetaData(): MetaData = TODO()
//    fun getFrameReader(): FrameReader = TODO()

    fun dispose() = dispose(nativeHandle)

    private external fun dispose(nativeHandle: Long)
}

class MetaData

class FrameReader {
    fun dispose(): Unit = TODO()
}

sealed class Request {
    abstract val url: String

//    // Expects a file://...
//    class File(val path: String) : Request()
//
//    // Follows the legacy behaviour of trying to reconstruct the file path and falling back to a content protocol
//    class PreferablyFile(val uri: String) : Request()
//
//    // Takes ownership of the descriptor, so no need to close it manually
//    class FileDescriptor(val descriptor: ParcelFileDescriptor) : Request()
//
//    // Takes ownership of the descriptor, so no need to close it manually
//    class Asset(val descriptor: AssetFileDescriptor) : Request()

    // Expects a content://...
    class Content(val uri: Uri) : Request() {
        override val url: String
            get() = uri.toString()
    }
}
