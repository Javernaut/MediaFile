#include <jni.h>

#include <MediaFile/MediaFile.hpp>

// This function is called when the native library is loaded.
jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    MediaFile::setJavaVM(vm);

    return JNI_VERSION_1_6;
}

// This function is called when the native library is unloaded.
void JNI_OnUnload(JavaVM *vm, void *reserved) {
    MediaFile::reset();
}
