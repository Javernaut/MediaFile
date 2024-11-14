//
// Created by Oleksandr Berezhnyi on 18.10.2024.
//
#include <jni.h>
#include <MediaFile/MediaFile.hpp>
#include <MediaFile/Logger.hpp>

extern "C"
JNIEXPORT void JNICALL
Java_io_github_javernaut_mediafile_factory_MediaFileFactory_nativeInitWith(
        JNIEnv *env,
        jobject thiz,
        jobject context
) {
    MediaFile::setAndroidContext(
            env->NewGlobalRef(context)
    );
}

extern "C"
JNIEXPORT void JNICALL
Java_io_github_javernaut_mediafile_factory_MediaFileFactory_nativeSetMinLogLevel(
        JNIEnv *env,
        jobject thiz,
        jint level
) {
    MediaFile::Logger::setMinLevel(static_cast<MediaFile::Logger::Level>(level));
}
