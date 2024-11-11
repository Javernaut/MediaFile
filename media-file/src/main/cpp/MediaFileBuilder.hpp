//
// Created by Oleksandr Berezhnyi on 20.10.2024.
//

#pragma once

#include <jni.h>
#include "MediaFileContext.hpp"

class MediaFileBuilder {
public:
    static void
    readMetaInfo(MediaFileContext *mediaFileContext, jobject jBuilder, jint mediaStreamsMask);

    static void init(JavaVM *vm);

    static void reset();
};
