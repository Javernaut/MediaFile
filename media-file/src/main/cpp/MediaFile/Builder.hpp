//
// Created by Oleksandr Berezhnyi on 20.10.2024.
//

#pragma once

#include <jni.h>
#include <MediaFile/Context.hpp>

namespace MediaFile {
    class Builder {
    public:
        static void readMetaData(
                MediaFile::Context *mediaFileContext,
                jobject jBuilder
        );

        static void init(JavaVM *vm);

        static void reset();
    };
}
