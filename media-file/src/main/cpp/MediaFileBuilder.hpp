//
// Created by Oleksandr Berezhnyi on 20.10.2024.
//

#ifndef WHATTHECODEC_MEDIAFILEBUILDER_HPP
#define WHATTHECODEC_MEDIAFILEBUILDER_HPP


#include <jni.h>
#include "MediaFileContext.hpp"

class MediaFileBuilder {
public:
    static void
    readMetaInfo(MediaFileContext *mediaFileContext, jobject jBuilder, jint mediaStreamsMask);

    static void init(JavaVM *vm);

    static void reset();
};


#endif //WHATTHECODEC_MEDIAFILEBUILDER_HPP
