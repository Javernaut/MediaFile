package io.github.javernaut.mediafile.factory

object Logger {

    fun setMinLevel(level: Level) {
        setMinLevel(level.ordinal)
    }

    private external fun setMinLevel(level: Int)

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
