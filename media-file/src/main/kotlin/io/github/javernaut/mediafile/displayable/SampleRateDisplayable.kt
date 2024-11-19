package io.github.javernaut.mediafile.displayable

import android.content.res.Resources
import io.github.javernaut.mediafile.R
import io.github.javernaut.mediafile.model.SampleRate
import java.text.DecimalFormat

/**
 * Returns prettier representation of [SampleRate] value, for example "48 kHz".
 */
fun SampleRate.toDisplayable(resources: Resources): String? {
    return if (value > 0) {
        val formattedSampleRate = DecimalFormat("0.#").format(value / 1000f)
        formattedSampleRate + " " + resources.getString(R.string.media_file_audio_sample_rate_postfix)
    } else {
        null
    }
}
