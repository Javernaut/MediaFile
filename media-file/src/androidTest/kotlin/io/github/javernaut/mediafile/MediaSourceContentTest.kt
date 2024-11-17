package io.github.javernaut.mediafile

import android.content.ContentValues
import android.provider.MediaStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith
import java.io.FileOutputStream
import java.io.IOException

// TODO Make runnable on Android 28 and lower
@RunWith(AndroidJUnit4::class)
class MediaSourceContentTest : DefaultMediaSourceTest() {

    @Test
    fun testVideoFile() {
        testContent(
            MediaFileAssertions.testVideoFileName,
            MediaFileAssertions::verifyVideoFile
        )
    }

    @Test
    fun testAudioFile() {
        testContent(
            MediaFileAssertions.testAudioFileName,
            MediaFileAssertions::verifyAudiFile
        )
    }

    private fun testContent(fileName: String, verify: (MediaFile?) -> Unit) {
        val context = InstrumentationRegistry.getInstrumentation().context
        val resolver = context.contentResolver

        val contentUri = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

        val entryDetails = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        }

        val fileUri = resolver.insert(contentUri, entryDetails)
            ?: throw IOException("Failed to insert file into MediaStore")

        try {
            resolver.openFileDescriptor(fileUri, "w")?.use { fd ->
                FileOutputStream(fd.fileDescriptor).use {
                    context.assets.open(fileName).copyTo(it)
                }
            } ?: throw IOException("Failed to open output stream")

            val mediaFile = factory.create(
                MediaSource.Content(fileUri),
            )

            verify(mediaFile)
        } finally {
            resolver.delete(fileUri, null)
        }
    }
}
