//
// Created by Oleksandr Berezhnyi on 20.10.2024.
//

#pragma once

#include <jni.h>
#include <MediaFile/Context.hpp>

namespace MediaFile {
    class Builder {
    public:
        static void readMetaInfo(
                MediaFile::Context *mediaFileContext,
                jobject jBuilder,
                jint mediaStreamsMask
        );

        static void init(JavaVM *vm);

        static void reset();
    };
}
