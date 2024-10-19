//
// Created by Oleksandr Berezhnyi on 18.10.2024.
//
#include <jni.h>
#include "MediaFileContext.h"
#include "MediaFileUtility.h"
#include "Reinterpret.h"

extern "C"
JNIEXPORT jlong JNICALL
Java_io_github_javernaut_mediafile_factory_MediaFileFactory_openContext(
        JNIEnv *env,
        jobject thiz,
        jstring url
) {
    const char *cUrl = env->GetStringUTFChars(url, nullptr);

    auto context = MediaFileContext::open(cUrl);

    env->ReleaseStringUTFChars(url, cUrl);

    return Reinterpret::toHandle(context);
}

extern "C"
JNIEXPORT void JNICALL
Java_io_github_javernaut_mediafile_factory_MediaFileFactory_nativeInitWith(
        JNIEnv *env,
        jobject thiz,
        jobject context
) {
    MediaFileUtility::setAndroidContext(
            env->NewGlobalRef(context)
    );
}
