package io.github.javernaut.mediafile.displayable

import android.content.res.Resources
import io.github.javernaut.mediafile.FrameRate
import io.github.javernaut.mediafile.R
import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.max

fun FrameRate.toDisplayable(resources: Resources): String? {
    return if (this.equalsDelta(0.0)) {
        null
    } else {
        format(this, resources)
    }
}

private fun format(number: Double, resources: Resources): String {
    val formattedFps = DecimalFormat("0.##").format(number)
    return "$formattedFps ${resources.getString(R.string.media_file_framerate_fps)}"
}

private fun Double.equalsDelta(other: Double) =
    abs(this - other) < max(Math.ulp(this), Math.ulp(other)) * 2
