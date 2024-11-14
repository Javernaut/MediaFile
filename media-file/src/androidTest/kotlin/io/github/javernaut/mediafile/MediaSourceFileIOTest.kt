package io.github.javernaut.mediafile

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.github.javernaut.mediafile.factory.MediaFileFactory
import io.github.javernaut.mediafile.factory.MediaSource
import io.github.javernaut.mediafile.factory.MediaType
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class MediaSourceFileIOTest {

    @Test
    fun testVideoFile() {
        testFile(
            MediaFileAssertions.testVideoFileName,
            MediaType.VIDEO,
            MediaFileAssertions::verifyVideoFile
        )
    }

    @Test
    fun testAudioFile() {
        testFile(
            MediaFileAssertions.testAudioFileName,
            MediaType.AUDIO,
            MediaFileAssertions::verifyAudiFile
        )
    }

    private fun testFile(fileName: String, mediaType: MediaType, verify: (MediaFile?) -> Unit) {
        val context = InstrumentationRegistry.getInstrumentation().context

        val internalDir = context.filesDir
        val dstFile = File(internalDir, fileName)

        try {
            val srcInputStream = context.assets.open(fileName)
            srcInputStream.copyTo(dstFile.outputStream())

            val mediaFile = MediaFileFactory.create(
                MediaSource.File(dstFile),
                mediaType
            )

            verify(mediaFile)
        } finally {
            dstFile.delete()
        }
    }
}
