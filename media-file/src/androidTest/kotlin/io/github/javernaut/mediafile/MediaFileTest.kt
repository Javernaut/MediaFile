package io.github.javernaut.mediafile

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import io.github.javernaut.mediafile.creator.MediaType
import io.github.javernaut.mediafile.factory.MediaFileFactory
import io.github.javernaut.mediafile.factory.MediaSource
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MediaFileTest {

    private val testVideoFileName = "test_video.mkv"
    private val testAudioFileName = "test_audio.aac"

    @Test
    fun testVideoFile() {
        val context = InstrumentationRegistry.getInstrumentation().context

        val assetFileDescriptor = context.assets.openFd(testVideoFileName)

        val mediaFileContext = MediaFileFactory.create(
            MediaSource.FileDescriptor(assetFileDescriptor, "matroska"),
            MediaType.VIDEO
        )

        assertThat(mediaFileContext).isNotNull()

        mediaFileContext.use {
            val mediaFile = mediaFileContext!!.readMetaData()

            assertThat(mediaFile).isNotNull()

            assertThat(mediaFile!!.fileFormatName).isEqualTo("Matroska / WebM")

            // Video stream
            assertThat(mediaFile.videoStream).isNotNull()

            val videoStream = mediaFile.videoStream!!

            assertThat(videoStream.basicInfo.index).isEqualTo(0)
            assertThat(videoStream.basicInfo.codecName).isEqualTo("H.264 / AVC / MPEG-4 AVC / MPEG-4 part 10")
            assertThat(videoStream.basicInfo.title).isNull()
            assertThat(videoStream.basicInfo.language).isNull()
            assertThat(videoStream.basicInfo.disposition).isEqualTo(1) // Aka 'Default'

            assertThat(videoStream.frameHeight).isEqualTo(2160)
            assertThat(videoStream.frameWidth).isEqualTo(3840)

            // Audio stream
            assertThat(mediaFile.audioStreams).isNotNull()
            assertThat(mediaFile.audioStreams).hasSize(1)

            val audioStream = mediaFile.audioStreams.first()
            assertThat(audioStream.basicInfo.index).isEqualTo(1)
            assertThat(audioStream.basicInfo.codecName).isEqualTo("ATSC A/52A (AC-3)")
            assertThat(audioStream.basicInfo.title).isNull()
            assertThat(audioStream.basicInfo.language).isNull()
            assertThat(audioStream.bitRate).isEqualTo(320000)
            assertThat(audioStream.sampleFormat).isEqualTo("fltp")
            assertThat(audioStream.sampleRate).isEqualTo(48000)
            assertThat(audioStream.channels).isEqualTo(6)
            assertThat(audioStream.channelLayout).isEqualTo("5.1(side)")
            assertThat(audioStream.basicInfo.disposition).isEqualTo(1) // Aka 'Default'

            // Subtitle stream
            assertThat(mediaFile.subtitleStreams).isNotNull()
            assertThat(mediaFile.subtitleStreams).hasSize(1)

            val subtitleStream = mediaFile.subtitleStreams.first()

            assertThat(subtitleStream.basicInfo.index).isEqualTo(2)
            assertThat(subtitleStream.basicInfo.codecName).isEqualTo("SubRip subtitle")
            assertThat(subtitleStream.basicInfo.title).isNull()
            assertThat(subtitleStream.basicInfo.language).isEqualTo("eng")
            assertThat(subtitleStream.basicInfo.disposition).isEqualTo(0)
        }
    }

    @Test
    fun testAudioFile() {
        val context = InstrumentationRegistry.getInstrumentation().context

        // Actual test
        val assetFileDescriptor = context.assets.openFd(testAudioFileName)

        val mediaFileContext = MediaFileFactory.create(
            MediaSource.FileDescriptor(assetFileDescriptor, "aac"),
            MediaType.AUDIO
        )

        assertThat(mediaFileContext).isNotNull()

        mediaFileContext.use {
            val mediaFile = mediaFileContext!!.readMetaData()

            assertThat(mediaFile).isNotNull()

            // Video stream
            assertThat(mediaFile!!.videoStream).isNull()

            // Audio stream
            assertThat(mediaFile.audioStreams).isNotNull()
            assertThat(mediaFile.audioStreams).hasSize(1)

            val audioStream = mediaFile.audioStreams.first()
            assertThat(audioStream.basicInfo.index).isEqualTo(0)
            assertThat(audioStream.basicInfo.codecName).isEqualTo("AAC (Advanced Audio Coding)")
            assertThat(audioStream.basicInfo.title).isNull()
            assertThat(audioStream.basicInfo.language).isNull()
            assertThat(audioStream.bitRate).isEqualTo(98625)
            assertThat(audioStream.sampleFormat).isEqualTo("fltp")
            assertThat(audioStream.sampleRate).isEqualTo(48000)
            assertThat(audioStream.channels).isEqualTo(1)
            assertThat(audioStream.channelLayout).isEqualTo("mono")
            assertThat(audioStream.basicInfo.disposition).isEqualTo(0)

            // Subtitle stream
            assertThat(mediaFile.subtitleStreams).isNotNull()
            assertThat(mediaFile.subtitleStreams).isEmpty()
        }
    }
}