package io.github.javernaut.mediafile

import android.content.ContentValues
import android.provider.MediaStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.github.javernaut.mediafile.factory.MediaFileFactory
import io.github.javernaut.mediafile.factory.MediaSource
import io.github.javernaut.mediafile.factory.MediaType
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.FileOutputStream
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MediaSourceContentTest {

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().context
        MediaFileFactory.initWith(context)
    }

    @Test
    fun testVideoFile() {
        testContent(
            MediaFileAssertions.testVideoFileName,
            MediaType.VIDEO,
            MediaFileAssertions::verifyVideoFile
        )
    }

    @Test
    fun testAudioFile() {
        testContent(
            MediaFileAssertions.testAudioFileName,
            MediaType.AUDIO,
            MediaFileAssertions::verifyAudiFile
        )
    }

    private fun testContent(fileName: String, mediaType: MediaType, verify: (MediaFile?) -> Unit) {
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

            val mediaFile = MediaFileFactory.create(
                MediaSource.Content(fileUri),
                mediaType
            )

            verify(mediaFile)
        } finally {
            resolver.delete(fileUri, null)
        }
    }
}
