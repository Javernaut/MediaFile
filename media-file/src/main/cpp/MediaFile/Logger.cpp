//
// Created by Oleksandr Berezhnyi on 12.11.2024.
//

#include <android/log.h>

#include <MediaFile/Logger.hpp>

extern "C" {
#include <libavutil/log.h>
}

static void ffmpeg_log_callback(void *ptr, int level, const char *fmt, va_list vl) {
    if (level > av_log_get_level()) {
        return;
    }
    int android_log_level;
    switch (level) {
        case AV_LOG_PANIC:
        case AV_LOG_FATAL:
            android_log_level = ANDROID_LOG_FATAL;
            break;
        case AV_LOG_ERROR:
            android_log_level = ANDROID_LOG_ERROR;
            break;
        case AV_LOG_WARNING:
            android_log_level = ANDROID_LOG_WARN;
            break;
        case AV_LOG_INFO:
            android_log_level = ANDROID_LOG_INFO;
            break;
        case AV_LOG_VERBOSE:
            android_log_level = ANDROID_LOG_VERBOSE;
            break;
        case AV_LOG_DEBUG:
        default:
            android_log_level = ANDROID_LOG_DEBUG;
            break;
    }

    __android_log_vprint(android_log_level, "MediaFile", fmt, vl);
}

namespace MediaFile {
    void Logger::setMinLevel(Level level) {
        int ffmpegLogLevel;
        switch (level) {
            case QUITE:
                ffmpegLogLevel = AV_LOG_QUIET;
                break;
            case FATAL:
                ffmpegLogLevel = AV_LOG_FATAL;
                break;
            case ERROR:
                ffmpegLogLevel = AV_LOG_ERROR;
                break;
            case WARN:
                ffmpegLogLevel = AV_LOG_WARNING;
                break;
            case VERBOSE:
                ffmpegLogLevel = AV_LOG_VERBOSE;
                break;
            case DEBUG:
            default:
                ffmpegLogLevel = AV_LOG_DEBUG;
                break;
        }
        av_log_set_level(ffmpegLogLevel);
    }

    void Logger::init() {
        av_log_set_callback(ffmpeg_log_callback);
        setMinLevel(Level::QUITE);
    }
} // MediaFile
