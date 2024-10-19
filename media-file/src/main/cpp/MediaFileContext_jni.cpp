//
// Created by Oleksandr Berezhnyi on 18.10.2024.
//
#include <jni.h>
#include "MediaFileContext.h"
#include "Reinterpret.h"

extern "C"
JNIEXPORT void JNICALL
Java_io_github_javernaut_mediafile_factory_MediaFileContext_dispose(
        JNIEnv *env,
        jobject thiz,
        jlong contextHandle
) {
    delete Reinterpret::fromHandle<MediaFileContext>(contextHandle);
}
