package io.github.javernaut.mediafile.model

/**
 * Represents common APIs for media streams of all types.
 */
interface MediaStream {
    /**
     * Returns the basic information of the stream.
     */
    val basicInfo: BasicStreamInfo
}
