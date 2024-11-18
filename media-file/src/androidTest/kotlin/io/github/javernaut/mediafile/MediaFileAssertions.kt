package io.github.javernaut.mediafile

import com.google.common.truth.Truth.assertThat
import io.github.javernaut.mediafile.model.BitRate
import io.github.javernaut.mediafile.model.SampleRate

object MediaFileAssertions {
    const val testVideoFileName = "test_video.mkv"
    const val testAudioFileName = "test_audio.aac"

    fun verifyVideoFile(mediaFile: MediaFile?) {
        assertThat(mediaFile).isNotNull()

        mediaFile.use {
            val mediaInfo = mediaFile!!.readMediaInfo()

            assertThat(mediaInfo).isNotNull()

            val container = mediaInfo!!.container
            assertThat(container.formatName).isEqualTo("Matroska / WebM")

            // Video stream
            assertThat(mediaInfo.videoStream).isNotNull()

            val videoStream = mediaInfo.videoStream!!

            assertThat(videoStream.basicInfo.index).isEqualTo(0)
            assertThat(videoStream.basicInfo.codecName).isEqualTo("H.264 / AVC / MPEG-4 AVC / MPEG-4 part 10")
            assertThat(videoStream.basicInfo.title).isNull()
            assertThat(videoStream.basicInfo.language).isNull()
            assertThat(videoStream.basicInfo.disposition).isEqualTo(1) // Aka 'Default'

            assertThat(videoStream.frameSize.height).isEqualTo(2160)
            assertThat(videoStream.frameSize.width).isEqualTo(3840)

            // Audio stream
            assertThat(mediaInfo.audioStreams).isNotNull()
            assertThat(mediaInfo.audioStreams).hasSize(1)

            val audioStream = mediaInfo.audioStreams.first()
            assertThat(audioStream.basicInfo.index).isEqualTo(1)
            assertThat(audioStream.basicInfo.codecName).isEqualTo("ATSC A/52A (AC-3)")
            assertThat(audioStream.basicInfo.title).isNull()
            assertThat(audioStream.basicInfo.language).isNull()
            assertThat(audioStream.bitRate).isEqualTo(BitRate(320000))
            assertThat(audioStream.sampleFormat).isEqualTo("fltp")
            assertThat(audioStream.sampleRate).isEqualTo(SampleRate(48000))
            assertThat(audioStream.channels).isEqualTo(6)
            assertThat(audioStream.channelLayout).isEqualTo("5.1(side)")
            assertThat(audioStream.basicInfo.disposition).isEqualTo(1) // Aka 'Default'

            // Subtitle stream
            assertThat(mediaInfo.subtitleStreams).isNotNull()
            assertThat(mediaInfo.subtitleStreams).hasSize(1)

            val subtitleStream = mediaInfo.subtitleStreams.first()

            assertThat(subtitleStream.basicInfo.index).isEqualTo(2)
            assertThat(subtitleStream.basicInfo.codecName).isEqualTo("SubRip subtitle")
            assertThat(subtitleStream.basicInfo.title).isNull()
            assertThat(subtitleStream.basicInfo.language).isEqualTo("eng")
            assertThat(subtitleStream.basicInfo.disposition).isEqualTo(0)
        }
    }

    fun verifyAudiFile(mediaFile: MediaFile?) {
        assertThat(mediaFile).isNotNull()

        mediaFile.use {
            val mediaInfo = mediaFile!!.readMediaInfo()

            assertThat(mediaInfo).isNotNull()

            // Video stream
            assertThat(mediaInfo!!.videoStream).isNull()

            // Audio stream
            assertThat(mediaInfo.audioStreams).isNotNull()
            assertThat(mediaInfo.audioStreams).hasSize(1)

            val audioStream = mediaInfo.audioStreams.first()
            assertThat(audioStream.basicInfo.index).isEqualTo(0)
            assertThat(audioStream.basicInfo.codecName).isEqualTo("AAC (Advanced Audio Coding)")
            assertThat(audioStream.basicInfo.title).isNull()
            assertThat(audioStream.basicInfo.language).isNull()
            assertThat(audioStream.bitRate).isEqualTo(BitRate(98625))
            assertThat(audioStream.sampleFormat).isEqualTo("fltp")
            assertThat(audioStream.sampleRate).isEqualTo(SampleRate(48000))
            assertThat(audioStream.channels).isEqualTo(1)
            assertThat(audioStream.channelLayout).isEqualTo("mono")
            assertThat(audioStream.basicInfo.disposition).isEqualTo(0)

            // Subtitle stream
            assertThat(mediaInfo.subtitleStreams).isNotNull()
            assertThat(mediaInfo.subtitleStreams).isEmpty()
        }
    }
}
