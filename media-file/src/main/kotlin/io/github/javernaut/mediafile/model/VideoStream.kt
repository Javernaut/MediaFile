package io.github.javernaut.mediafile.model

import android.util.Size

/**
 * Represents metadata of a video stream in a video file.
 */
data class VideoStream(
    override val basicInfo: BasicStreamInfo,

    /**
     * Bit rate.
     */
    val bitRate: BitRate,

    /**
     * Guessed frame rate. Can be 0 in case it couldn't be determined.
     */
    val frameRate: FrameRate,

    /**
     * Original frame size.
     */
    val frameSize: Size,
) : MediaStream

typealias FrameRate = Double
