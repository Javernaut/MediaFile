#include <android/log.h>

extern "C" {
#include <libavutil/log.h>
}

#ifndef NDEBUG

void ffmpeg_log_callback(void *ptr, int level, const char *fmt, va_list vl) {
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

    __android_log_vprint(android_log_level, "FFmpeg", fmt, vl);
}

void bind_ffmpeg_logs_to_logcat() {
    av_log_set_callback(ffmpeg_log_callback);
}

#else

void bind_ffmpeg_logs_to_logcat() {}

#endif
