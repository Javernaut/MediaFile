//
// Created by Oleksandr Berezhnyi on 19.10.2024.
//

#include <jni.h>
#include <MediaFile/Builder.hpp>
#include <MediaFile/MediaFile.hpp>
#include <MediaFile/Logger.hpp>

extern "C" {
#include <libavcodec/jni.h>
}

void MediaFile::setAndroidContext(void *androidContext) {
    av_jni_set_android_app_ctx(androidContext, nullptr);
}

void MediaFile::setJavaVM(JavaVM *vm) {
    av_jni_set_java_vm(vm, nullptr);
    MediaFile::Logger::init();
    MediaFile::Builder::init(vm);
}

void MediaFile::reset() {
    setAndroidContext(nullptr);
    setJavaVM(nullptr);
    MediaFile::Builder::reset();
}
