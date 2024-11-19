package io.github.javernaut.mediafile

/**
 * Enum for the minimum logging level of FFmpeg events to post to LogCat. The values resemble the levels used in Android's Log class.
 *
 * The [QUITE] value is used for disabling the logging.
 */
enum class LogLevel {
    QUITE, // No logging
    FATAL,
    ERROR,
    WARN,
    VERBOSE,
    DEBUG
}
