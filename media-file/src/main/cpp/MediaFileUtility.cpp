//
// Created by Oleksandr Berezhnyi on 19.10.2024.
//

#include "MediaFileUtility.h"

extern "C" {
#include <libavcodec/jni.h>
}

void MediaFileUtility::setAndroidContext(void *androidContext) {
    av_jni_set_android_app_ctx(androidContext, nullptr);
}

void MediaFileUtility::setJavaVM(void *vm) {
    av_jni_set_java_vm(vm, nullptr);
}

void MediaFileUtility::reset() {
    setAndroidContext(nullptr);
    setJavaVM(nullptr);
}
