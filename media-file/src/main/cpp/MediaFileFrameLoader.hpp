//
// Created by Oleksandr Berezhnyi on 20.10.2024.
//

#pragma once

#include <jni.h>
#include "MediaFile/Context.hpp"

extern "C" {
#include <libavformat/avformat.h>
};

class MediaFileFrameLoader {
public:
    // Root FFmpeg object for the given media.
    AVFormatContext *avFormatContext;
    // Parameters of a video stream.
    const AVCodecParameters *parameters;
    // Codec of a video stream.
    const AVCodec *avVideoCodec;
    // And index of a video stream in the avFormatContext.
    const int videoStreamIndex;
    const int totalFramesToRead;
    int frameToRead = 0;

    explicit MediaFileFrameLoader(
            AVFormatContext *avFormatContext,
            AVCodecParameters *parameters,
            const AVCodec *avVideoCodec,
            int videoStreamIndex,
            int totalFramesToRead
    );

    bool loadFrame(JNIEnv *env, jobject bitmap);

    static MediaFileFrameLoader *
    create(MediaFile::Context *mediaFileContext, int totalFramesToRead);
};
