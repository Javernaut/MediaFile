package io.github.javernaut.mediafile.model

/**
 * Represents metadata of a subtitle stream in a media file.
 */
data class SubtitleStream(
    override val basicInfo: BasicStreamInfo
) : MediaStream
