//
// Created by Oleksandr Berezhnyi on 19.10.2024.
//

#pragma once

#include <jni.h>

namespace Reinterpret {
    static jlong NO_REF = 0;

    template<typename T>
    T *fromHandle(jlong handle) {
        return handle == NO_REF ? nullptr : reinterpret_cast<T *>(handle);
    }

    template<typename T>
    jlong toHandle(T *ptr) {
        return ptr == nullptr ? NO_REF : reinterpret_cast<jlong>(ptr);
    }
}
