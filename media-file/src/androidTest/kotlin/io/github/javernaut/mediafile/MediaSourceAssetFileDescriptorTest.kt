package io.github.javernaut.mediafile

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.github.javernaut.mediafile.factory.MediaFileFactory
import io.github.javernaut.mediafile.factory.MediaSource
import io.github.javernaut.mediafile.factory.MediaType
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MediaSourceAssetFileDescriptorTest {

    private val testVideoFileName = "test_video.mkv"
    private val testAudioFileName = "test_audio.aac"

    @Test
    fun testVideoFile() {
        val context = InstrumentationRegistry.getInstrumentation().context

        val assetFileDescriptor = context.assets.openFd(testVideoFileName)

        val mediaFile = MediaFileFactory.create(
            MediaSource.FileDescriptor(assetFileDescriptor, "matroska"),
            MediaType.VIDEO
        )

        MediaFileAssertions.verifyVideoFile(mediaFile)
    }

    @Test
    fun testAudioFile() {
        val context = InstrumentationRegistry.getInstrumentation().context

        // Actual test
        val assetFileDescriptor = context.assets.openFd(testAudioFileName)

        val mediaFile = MediaFileFactory.create(
            MediaSource.FileDescriptor(assetFileDescriptor, "aac"),
            MediaType.AUDIO
        )

        MediaFileAssertions.verifyAudiFile(mediaFile)
    }
}
