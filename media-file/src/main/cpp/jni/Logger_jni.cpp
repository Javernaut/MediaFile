//
// Created by Oleksandr Berezhnyi on 18.10.2024.
//
#include <jni.h>
#include <MediaFile/Logger.hpp>

extern "C"
JNIEXPORT void JNICALL
Java_io_github_javernaut_mediafile_factory_Logger_setMinLevel(
        JNIEnv *env,
        jobject thiz,
        jint level
) {
    MediaFile::Logger::setMinLevel(static_cast<MediaFile::Logger::Level>(level));
}
