//
// Created by Oleksandr Berezhnyi on 20.10.2024.
//

#include <jni.h>
#include <MediaFile/FrameLoader.hpp>
#include <jni/Reinterpret.hpp>

extern "C"
JNIEXPORT jboolean JNICALL
Java_io_github_javernaut_mediafile_MediaFileFrameLoader_nativeLoadFrame(
        JNIEnv *env,
        jclass clazz,
        jlong handle,
        jobject bitmap
) {
    auto frameLoader = Reinterpret::fromHandle<MediaFile::FrameLoader>(handle);
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
    auto mediaFileFrameLoader = MediaFile::FrameLoader::create(mediaFileContext, totalFramesToRead);
    return Reinterpret::toHandle(mediaFileFrameLoader);
}

extern "C"
JNIEXPORT void JNICALL
Java_io_github_javernaut_mediafile_MediaFileFrameLoader_dispose(
        JNIEnv *env,
        jclass clazz,
        jlong frameLoaderHandle
) {
    delete Reinterpret::fromHandle<MediaFile::FrameLoader>(frameLoaderHandle);
}
