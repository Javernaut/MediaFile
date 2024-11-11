#include <jni.h>

#include "log/ffmpeg.h"
#include "MediaFileUtility.h"

// This function is called when the native library is loaded.
jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    bind_ffmpeg_logs_to_logcat();

    MediaFileUtility::setJavaVM(vm);

    return JNI_VERSION_1_6;
}

// This function is called when the native library is unloaded.
void JNI_OnUnload(JavaVM *vm, void *reserved) {
    MediaFileUtility::reset();
}
