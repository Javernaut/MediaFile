package io.github.javernaut.mediafile

import android.content.res.AssetFileDescriptor
import android.net.Uri
import android.os.ParcelFileDescriptor

/**
 * A sealed hierarchy of media sources, from which a [MediaFileFactory] can create [MediaFile] instances.
 */
sealed class MediaSource {

    internal abstract fun openContext(): NativeHandle

    protected companion object {
        @JvmStatic
        external fun nativeOpenUrl(url: String): NativeHandle // MediaFileContext

        @JvmStatic
        external fun nativeOpenFileDescriptor(
            fileDescriptor: Int,
            skipInitialBytes: Long,
            formatNameHint: String?
        ): NativeHandle // MediaFileContext
    }

    /**
     * Regular Android's [Uri]s with `content://` scheme.
     *
     * Backed by FFmpeg's _android_content_ protocol.
     */
    class Content(private val uri: Uri) : MediaSource() {
        override fun openContext() = nativeOpenUrl(uri.toString())
    }

    /**
     * Standard [java.io.File] objects. Even with the Scoped Storage these are still usable for app's own directories.
     *
     * Backed by FFmpeg's _file_ protocol.
     */
    class File(file: java.io.File) : MediaSource() {

        private val filePath: String = file.absolutePath

        override fun openContext() = nativeOpenUrl("file://$filePath")
    }

    // Takes ownership of the descriptor. No need to close it manually
    // Backed by the fd: protocol

    /**
     * Accepts a file descriptor in a form of a [ParcelFileDescriptor] or an [AssetFileDescriptor].
     *
     * On successful [MediaFile] creation, the ownership of the file descriptor is transferred to the [MediaFile]'s native implementation. In this case the file descriptor is released by the [MediaFile.close] method call.
     *
     * In case the [FileDescriptor] instance wasn't used or if no [MediaFile] instance was retrieved, the incoming [ParcelFileDescriptor] or [AssetFileDescriptor] should be released manually.
     *
     * Backed by FFmpeg's _fd_ protocol.
     */
    class FileDescriptor : MediaSource {

        private val fileDescriptor: ParcelFileDescriptor

        private var skipInitialBytes = 0L
        private var formatNameHint: String? = null

        /**
         * Creates a [FileDescriptor] instance from a [ParcelFileDescriptor]. See class documentation for more details.
         */
        constructor(fileDescriptor: ParcelFileDescriptor) {
            this.fileDescriptor = fileDescriptor
        }

        /**
         * Creates a [FileDescriptor] instance from an [AssetFileDescriptor]. See class documentation for more details.
         *
         * @param formatNameHint in certain cases the container format isn't properly read by FFmpeg.
         * Given asset files are something that is known in advance, by supplying this parameter FFmpeg picks the exact format to work with.
         * The list of available formats can be found in the [FFmpeg documentation](https://ffmpeg.org/ffmpeg-formats.html).
         */
        constructor(
            assetFileDescriptor: AssetFileDescriptor,
            formatNameHint: String,
        ) : this(assetFileDescriptor.parcelFileDescriptor) {
            this.skipInitialBytes = assetFileDescriptor.startOffset
            this.formatNameHint = formatNameHint
        }

        override fun openContext(): NativeHandle {
            val result =
                nativeOpenFileDescriptor(fileDescriptor.fd, skipInitialBytes, formatNameHint)
            if (result.isValid) {
                // Once FFmpeg took the ownership of the file descriptor, we need to detach it here
                fileDescriptor.detachFd()
            }
            return result
        }
    }
}
