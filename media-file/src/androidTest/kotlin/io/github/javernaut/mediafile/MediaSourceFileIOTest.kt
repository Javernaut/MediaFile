package io.github.javernaut.mediafile

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class MediaSourceFileIOTest : DefaultMediaSourceTest() {

    @Test
    fun testVideoFile() {
        testFile(
            MediaFileAssertions.testVideoFileName,
            MediaFileAssertions::verifyVideoFile
        )
    }

    @Test
    fun testAudioFile() {
        testFile(
            MediaFileAssertions.testAudioFileName,
            MediaFileAssertions::verifyAudiFile
        )
    }

    private fun testFile(fileName: String, verify: (MediaFile?) -> Unit) {
        val context = InstrumentationRegistry.getInstrumentation().context

        val internalDir = context.filesDir
        val dstFile = File(internalDir, fileName)

        try {
            val srcInputStream = context.assets.open(fileName)
            srcInputStream.copyTo(dstFile.outputStream())

            val mediaFile = factory.create(
                MediaSource.File(dstFile)
            )

            verify(mediaFile)
        } finally {
            dstFile.delete()
        }
    }
}
