//
// Created by Oleksandr Berezhnyi on 18.10.2024.
//
#include <jni.h>
#include <MediaFile/Context.hpp>
#include <MediaFile/Builder.hpp>
#include <jni/Reinterpret.hpp>

extern "C"
JNIEXPORT void JNICALL
Java_io_github_javernaut_mediafile_MediaFile_nativeClose(
        JNIEnv *env,
        jobject thiz,
        jlong contextHandle
) {
    delete Reinterpret::fromHandle<MediaFile::Context>(contextHandle);
}

extern "C"
JNIEXPORT void JNICALL
Java_io_github_javernaut_mediafile_MediaFile_nativeReadMetaData(
        JNIEnv *env,
        jobject thiz,
        jlong contextHandle,
        jobject jBuilder
) {
    MediaFile::Builder::readMetaData(
            Reinterpret::fromHandle<MediaFile::Context>(contextHandle),
            jBuilder
    );
}
