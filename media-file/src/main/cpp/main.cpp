#include <jni.h>

#include "utils.h"
#include "log/ffmpeg.h"

// This function is called when the native library is loaded.
jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    if (utils_fields_init(vm) != 0) {
        return -1;
    }

    bind_ffmpeg_logs_to_logcat();

    return JNI_VERSION_1_6;
}

// This function is called when the native library is unloaded.
void JNI_OnUnload(JavaVM *vm, void *reserved) {
    utils_fields_free(vm);
}
