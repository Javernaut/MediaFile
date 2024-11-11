//
// Created by Oleksandr Berezhnyi on 18.10.2024.
//
#include <jni.h>
#include <MediaFile/Context.hpp>
#include <MediaFile/Builder.hpp>
#include <jni/Reinterpret.hpp>

extern "C"
JNIEXPORT void JNICALL
Java_io_github_javernaut_mediafile_factory_MediaFileContext_dispose(
        JNIEnv *env,
        jobject thiz,
        jlong contextHandle
) {
    delete Reinterpret::fromHandle<MediaFile::Context>(contextHandle);
}

extern "C"
JNIEXPORT void JNICALL
Java_io_github_javernaut_mediafile_factory_MediaFileContext_readMetaData(
        JNIEnv *env,
        jobject thiz,
        jlong contextHandle,
        jobject jBuilder,
        jint mediaStreamsMask
) {
    MediaFile::Builder::readMetaInfo(
            Reinterpret::fromHandle<MediaFile::Context>(contextHandle),
            jBuilder,
            mediaStreamsMask
    );
}
