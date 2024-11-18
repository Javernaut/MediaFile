package io.github.javernaut.mediafile.model

/**
 * A structure that has metadata of a video or an audio file and its media streams.
 */
data class MetaData(
    val container: Container,
    val videoStream: VideoStream?,
    val audioStreams: List<AudioStream>,
    val subtitleStreams: List<SubtitleStream>
)
