package io.github.javernaut.mediafile.displayable

import android.content.res.Resources
import io.github.javernaut.mediafile.R
import io.github.javernaut.mediafile.model.BasicStreamInfo

/**
 * Returns a comma separated concatenation of each disposition bits meanings, for example "Default, Forced".
 */
fun BasicStreamInfo.getDisplayableDisposition(resources: Resources): String? {
    return if (disposition != 0) {
        DispositionFeature.entries.filter {
            disposition and it.mask != 0
        }.joinToString(separator = ", ") {
            resources.getString(it.stringIdRes)
        }
    } else {
        null
    }
}

/**
 * Class that maps a certain bit in a 'disposition' integer to a string res id.
 * Values for masks got from libavformat/avformat.h.
 */
private enum class DispositionFeature(val mask: Int, val stringIdRes: Int) {
    DEFAULT(0x0001, R.string.media_file_disposition_default),
    DUB(0x0002, R.string.media_file_disposition_dub),
    ORIGINAL(0x0004, R.string.media_file_disposition_original),
    COMMENT(0x0008, R.string.media_file_disposition_comment),
    LYRICS(0x0010, R.string.media_file_disposition_lyrics),
    KARAOKE(0x0020, R.string.media_file_disposition_karaoke),
    FORCED(0x0040, R.string.media_file_disposition_forced),
    HEARING_IMPAIRED(0x0080, R.string.media_file_disposition_hearing_impaired),
    VISUAL_IMPAIRED(0x0100, R.string.media_file_disposition_visual_impaired),
    CLEAN_EFFECTS(0x0200, R.string.media_file_disposition_clean_effects),
    ATTACHED_PIC(0x0400, R.string.media_file_disposition_attached_pic),
    TIMED_THUMBNAILS(0x0800, R.string.media_file_disposition_timed_thumbnails)
}
