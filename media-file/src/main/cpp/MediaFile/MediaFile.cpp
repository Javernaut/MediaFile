//
// Created by Oleksandr Berezhnyi on 19.10.2024.
//

#include <jni.h>
#include <MediaFile/Builder.hpp>
#include <MediaFile/MediaFile.hpp>
#include <log/ffmpeg.h>

extern "C" {
#include <libavcodec/jni.h>
}

void MediaFile::setAndroidContext(void *androidContext) {
    av_jni_set_android_app_ctx(androidContext, nullptr);
}

void MediaFile::setJavaVM(JavaVM *vm) {
    av_jni_set_java_vm(vm, nullptr);
    bind_ffmpeg_logs_to_logcat();
    MediaFile::Builder::init(vm);
}

void MediaFile::reset() {
    setAndroidContext(nullptr);
    setJavaVM(nullptr);
    MediaFile::Builder::reset();
}
