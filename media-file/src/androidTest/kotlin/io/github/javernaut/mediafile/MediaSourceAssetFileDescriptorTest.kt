package io.github.javernaut.mediafile

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MediaSourceAssetFileDescriptorTest : DefaultMediaSourceTest() {

    @Test
    fun testVideoFile() {
        testAssetFileDescriptor(
            MediaFileAssertions.testVideoFileName,
            "matroska",
            MediaType.VIDEO,
            MediaFileAssertions::verifyVideoFile
        )
    }

    @Test
    fun testAudioFile() {
        testAssetFileDescriptor(
            MediaFileAssertions.testAudioFileName,
            "aac",
            MediaType.AUDIO,
            MediaFileAssertions::verifyAudiFile
        )
    }

    private fun testAssetFileDescriptor(
        fileName: String,
        formatNameHint: String,
        mediaType: MediaType,
        verify: (MediaFile?) -> Unit
    ) {
        val context = InstrumentationRegistry.getInstrumentation().context

        val assetFileDescriptor = context.assets.openFd(fileName)

        val mediaFile = factory.create(
            MediaSource.FileDescriptor(assetFileDescriptor, formatNameHint),
            mediaType
        )

        verify(mediaFile)
    }
}
