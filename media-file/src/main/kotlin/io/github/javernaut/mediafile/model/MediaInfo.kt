package io.github.javernaut.mediafile.model

/**
 * A structure that has metadata of a video or audio file and its media streams.
 */
data class MediaInfo(
    val container: Container,
    val videoStream: VideoStream?,
    val audioStreams: List<AudioStream>,
    val subtitleStreams: List<SubtitleStream>
)
