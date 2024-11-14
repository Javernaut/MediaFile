package io.github.javernaut.mediafile

object Logger {

    fun setMinLevel(level: Level) {
        nativeSetMinLevel(level.ordinal)
    }

    private external fun nativeSetMinLevel(level: Int)

    enum class Level {
        // No logging
        QUITE,
        FATAL,
        ERROR,
        WARN,
        VERBOSE,
        DEBUG
    }
}
