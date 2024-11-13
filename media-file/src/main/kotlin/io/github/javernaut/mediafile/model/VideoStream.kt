package io.github.javernaut.mediafile.model

/**
 * Represents metadata of a video stream in a video file.
 */
class VideoStream internal constructor(
    override val basicInfo: BasicStreamInfo,
    val bitRate: BitRate,

    /**
     * Guessed frame rate. Can be 0 in case it couldn't be determined.
     */
    val frameRate: FrameRate,
    val frameWidth: Int,
    val frameHeight: Int
) : MediaStream

typealias FrameRate = Double
