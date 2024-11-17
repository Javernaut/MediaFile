package io.github.javernaut.mediafile

import android.content.res.AssetFileDescriptor
import android.net.Uri
import android.os.ParcelFileDescriptor

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

    // Expects a content://...
    class Content(private val uri: Uri) : MediaSource() {
        override fun openContext() = nativeOpenUrl(uri.toString())
    }

    // Expects a file://...
    class File(file: java.io.File) : MediaSource() {

        private val filePath: String = file.absolutePath

        override fun openContext() = nativeOpenUrl("file://$filePath")
    }

    // Takes ownership of the descriptor. No need to close it manually
    // Backed by the fd: protocol
    class FileDescriptor(
        private val fileDescriptor: ParcelFileDescriptor
    ) : MediaSource() {

        private var skipInitialBytes = 0L
        private var formatNameHint: String? = null

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
