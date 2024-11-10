package io.github.javernaut.mediafile.factory

import android.content.res.AssetFileDescriptor
import android.net.Uri
import android.os.ParcelFileDescriptor

sealed class Request {

    protected abstract val url: String

    internal open fun openContext(): NativeHandle {
        return openUrl(url)
    }

    protected companion object {
        @JvmStatic
        external fun openUrl(url: String): NativeHandle // MediaFileContext

        @JvmStatic
        external fun openAsset(
            url: String,
            skipInitialBytes: Long,
            formatNameHint: String
        ): NativeHandle // MediaFileContext
    }
}

// Expects a file://...
class File(override val url: String) : Request()

// Takes ownership of the descriptor, so no need to close it manually
class FileDescriptor(
    private val fileDescriptor: ParcelFileDescriptor,
    protocol: Protocol
) :
    BaseFileDescriptor(protocol) {

    override val descriptor: Int
        get() = fileDescriptor.detachFd()
}

// Takes ownership of the descriptor, so no need to close it manually
class Asset(
    private val assetFileDescriptor: AssetFileDescriptor,
    private val formatNameHint: String,
    protocol: Protocol
) : BaseFileDescriptor(protocol) {

    override val descriptor: Int
        get() = assetFileDescriptor.parcelFileDescriptor.detachFd()

    override fun openContext(): NativeHandle {
        return openAsset(url, assetFileDescriptor.startOffset, formatNameHint)
    }
}

// Expects a content://...
class Content(private val uri: Uri) : Request() {
    override val url: String
        get() = uri.toString()
}

abstract class BaseFileDescriptor(val protocol: Protocol) : Request() {
    abstract val descriptor: Int

    override val url: String
        get() = when (protocol) {
            Protocol.FD -> "fd:$descriptor"
            Protocol.PIPE -> "pipe:$descriptor"
        }
}
