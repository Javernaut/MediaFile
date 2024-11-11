//
// Created by Oleksandr Berezhnyi on 19.10.2024.
//

#include <jni.h>
#include <MediaFile/Builder.hpp>
#include "MediaFileUtility.hpp"

extern "C" {
#include <libavcodec/jni.h>
}

void MediaFileUtility::setAndroidContext(void *androidContext) {
    av_jni_set_android_app_ctx(androidContext, nullptr);
}

void MediaFileUtility::setJavaVM(JavaVM *vm) {
    av_jni_set_java_vm(vm, nullptr);
    MediaFile::Builder::init(vm);
}

void MediaFileUtility::reset() {
    setAndroidContext(nullptr);
    setJavaVM(nullptr);
    MediaFile::Builder::reset();
}
