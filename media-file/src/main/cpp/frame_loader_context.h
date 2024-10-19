//
// Created by Oleksandr Berezhnyi on 14/10/19.
//

#ifndef WHATTHECODEC_FRAME_LOADER_CONTEXT_H
#define WHATTHECODEC_FRAME_LOADER_CONTEXT_H

#include <jni.h>

extern "C" {
#include <libavformat/avformat.h>
#include <libavcodec/avcodec.h>
}

/**
 * A struct that is stored in a MediaFile.frameLoader.frameLoadingContextHandle.
 * Aggregates necessary pointers to FFmpeg structs.
 */
struct FrameLoaderContext {
    // Root FFmpeg object for the given media.
    AVFormatContext *avFormatContext;
    // Parameters of a video stream.
    AVCodecParameters *parameters;
    // Codec of a video stream.
    const AVCodec *avVideoCodec;
    // And index of a video stream in the avFormatContext.
    int videoStreamIndex;
};

/**
 * Frees the FrameLoaderContext struct.
 *
 * @param handle a pointer to a FrameLoaderContext struct to free
 */
void frame_loader_context_free(int64_t handle);

#endif //WHATTHECODEC_FRAME_LOADER_CONTEXT_H
