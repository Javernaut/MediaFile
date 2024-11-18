package io.github.javernaut.mediafile.model

/**
 * A structure that has metadata of a video or an audio file and its media streams.
 */
data class MediaInfo(
    /**
     * Information about the container.
     */
    val container: Container,

    /**
     * An optional video stream's data.
     */
    val videoStream: VideoStream?,

    /**
     * List of available audio streams.
     */
    val audioStreams: List<AudioStream>,

    /**
     * List of available subtitle streams.
     */
    val subtitleStreams: List<SubtitleStream>
)
