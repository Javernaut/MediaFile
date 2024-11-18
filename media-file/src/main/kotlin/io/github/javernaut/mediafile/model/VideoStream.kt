package io.github.javernaut.mediafile.model

import android.util.Size
import io.github.javernaut.mediafile.displayable.toDisplayable

/**
 * Represents metadata of a video stream in a video file.
 */
data class VideoStream(
    override val basicInfo: BasicStreamInfo,

    /**
     * Bit rate.
     *
     * Can be converted to a human readable string using [BitRate.toDisplayable].
     */
    val bitRate: BitRate,

    /**
     * Guessed frame rate. Can be 0 in case it couldn't be determined.
     *
     * Can be converted to a human readable string using [FrameRate.toDisplayable].
     */
    val frameRate: FrameRate,

    /**
     * Original frame size.
     */
    val frameSize: Size,
) : MediaStream

@JvmInline
value class FrameRate(val value: Double)
