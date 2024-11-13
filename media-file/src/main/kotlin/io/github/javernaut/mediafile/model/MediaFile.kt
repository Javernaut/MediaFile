package io.github.javernaut.mediafile.model

/**
 * A structure that has metadata of a video or audio file and its media streams.
 */
class MediaFile internal constructor(
    val fileFormatName: String,
    val videoStream: VideoStream?,
    val audioStreams: List<AudioStream>,
    val subtitleStreams: List<SubtitleStream>
) {

    /**
     * true if file is accessed by file: protocol and false if by pipe:
     */
    // TODO Replace with the protocol used
    val fullFeatured = false
}
