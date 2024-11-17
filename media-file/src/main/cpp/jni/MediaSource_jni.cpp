//
// Created by Oleksandr Berezhnyi on 10.11.2024.
//
#include <jni.h>
#include <MediaFile/Context.hpp>
#include <jni/Reinterpret.hpp>

extern "C"
JNIEXPORT jlong JNICALL
Java_io_github_javernaut_mediafile_MediaSource_nativeOpenUrl(JNIEnv *env, jclass clazz,
                                                                     jstring url) {
    const char *cUrl = env->GetStringUTFChars(url, nullptr);

    auto context = MediaFile::Context::open(cUrl);

    env->ReleaseStringUTFChars(url, cUrl);

    return Reinterpret::toHandle(context);
}

extern "C"
JNIEXPORT jlong JNICALL
Java_io_github_javernaut_mediafile_MediaSource_nativeOpenFileDescriptor(
        JNIEnv *env,
        jclass clazz,
        jint file_descriptor,
        jlong skip_initial_bytes,
        jstring format_name_hint
) {
    const char *cFormatName =
            format_name_hint == nullptr
            ? nullptr
            : env->GetStringUTFChars(format_name_hint, nullptr);

    auto result = MediaFile::Context::open(file_descriptor, skip_initial_bytes, cFormatName);

    if (cFormatName != nullptr) {
        env->ReleaseStringUTFChars(format_name_hint, cFormatName);
    }

    return Reinterpret::toHandle(result);;
}
