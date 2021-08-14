package io.github.javernaut.mediafile

/**
 * Represents metadata of a video stream in a video file.
 */
class VideoStream internal constructor(
    val basicInfo: BasicStreamInfo,
    val bitRate: BitRate,
    val frameWidth: Int,
    val frameHeight: Int
)
