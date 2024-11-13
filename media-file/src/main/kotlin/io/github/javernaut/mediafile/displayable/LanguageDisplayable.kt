package io.github.javernaut.mediafile.displayable

import io.github.javernaut.mediafile.model.BasicStreamInfo
import java.util.Locale

val BasicStreamInfo.displayableLanguage: String?
    get() = language?.let {
        val capitalizedLanguage = (Locale.forLanguageTag(language).getDisplayLanguage(Locale.US)
            ?: language).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

        if (capitalizedLanguage.isNotEmpty()) {
            return capitalizedLanguage
        }
        return null
    }
