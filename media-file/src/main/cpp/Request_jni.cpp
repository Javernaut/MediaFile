//
// Created by Oleksandr Berezhnyi on 10.11.2024.
//
#include <jni.h>
#include <functional>
#include "MediaFileContext.h"
#include "Reinterpret.h"

static jlong openUrl(
        JNIEnv *env,
        jstring url,
        const std::function<MediaFileContext *(const char *)> &producer
) {
    const char *cUrl = env->GetStringUTFChars(url, nullptr);

    auto context = producer(cUrl);

    env->ReleaseStringUTFChars(url, cUrl);

    return Reinterpret::toHandle(context);
}

extern "C"
JNIEXPORT jlong JNICALL
Java_io_github_javernaut_mediafile_factory_Request_openUrl(JNIEnv *env, jclass clazz,
                                                           jstring url) {
    return openUrl(env, url, [](const char *cUrl) -> MediaFileContext * {
        return MediaFileContext::open(cUrl);
    });
}

extern "C"
JNIEXPORT jlong JNICALL
Java_io_github_javernaut_mediafile_factory_Request_openAsset(JNIEnv *env, jclass clazz,
                                                             jstring url,
                                                             jlong skip_initial_bytes,
                                                             jstring format_name_hint) {
    const char *cName = env->GetStringUTFChars(format_name_hint, nullptr);

    auto result = openUrl(env, url,
                          [skip_initial_bytes, cName](const char *cUrl) -> MediaFileContext * {
                              return MediaFileContext::open(cUrl, skip_initial_bytes, cName);
                          });

    env->ReleaseStringUTFChars(format_name_hint, cName);

    return result;
}
