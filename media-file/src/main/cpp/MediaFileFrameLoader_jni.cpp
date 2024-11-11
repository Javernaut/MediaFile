//
// Created by Oleksandr Berezhnyi on 20.10.2024.
//

#include <jni.h>
#include "Reinterpret.hpp"
#include "MediaFileFrameLoader.hpp"

extern "C"
JNIEXPORT jboolean JNICALL
Java_io_github_javernaut_mediafile_MediaFileFrameLoader_nativeLoadFrame(
        JNIEnv *env,
        jclass clazz,
        jlong handle,
        jobject bitmap
) {
    auto frameLoader = Reinterpret::fromHandle<MediaFileFrameLoader>(handle);
    return frameLoader->loadFrame(env, bitmap);
}


extern "C"
JNIEXPORT jlong JNICALL
Java_io_github_javernaut_mediafile_MediaFileFrameLoader_nativeCreateFrameLoader(
        JNIEnv *env,
        jclass clazz,
        jlong contextHandle,
        jint totalFramesToRead
) {
    auto mediaFileContext = Reinterpret::fromHandle<MediaFile::Context>(contextHandle);
    auto mediaFileFrameLoader = MediaFileFrameLoader::create(mediaFileContext, totalFramesToRead);
    return Reinterpret::toHandle(mediaFileFrameLoader);
}

extern "C"
JNIEXPORT void JNICALL
Java_io_github_javernaut_mediafile_MediaFileFrameLoader_dispose(
        JNIEnv *env,
        jclass clazz,
        jlong frameLoaderHandle
) {
    delete Reinterpret::fromHandle<MediaFileFrameLoader>(frameLoaderHandle);
}
