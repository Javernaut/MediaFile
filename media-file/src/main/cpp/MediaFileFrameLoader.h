//
// Created by Oleksandr Berezhnyi on 20.10.2024.
//

#ifndef WHATTHECODEC_MEDIAFILEFRAMELOADER_H
#define WHATTHECODEC_MEDIAFILEFRAMELOADER_H

#include <jni.h>
#include "MediaFileContext.h"

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

    bool loadFrame(jobject bitmap);

    static MediaFileFrameLoader *create(MediaFileContext *mediaFileContext, int totalFramesToRead);
};


#endif //WHATTHECODEC_MEDIAFILEFRAMELOADER_H