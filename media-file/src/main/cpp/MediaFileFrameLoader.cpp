//
// Created by Oleksandr Berezhnyi on 20.10.2024.
//

#include "MediaFileFrameLoader.h"

int get_video_stream_index(AVFormatContext *formatContext) {
    for (int i = 0; i < formatContext->nb_streams; ++i) {
        if (formatContext->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
            return i;
        }
    }
    return -1; // No video stream found
}

MediaFileFrameLoader *MediaFileFrameLoader::create(
        MediaFileContext *mediaFileContext,
        int totalFramesToRead
) {
    AVFormatContext *avFormatContext = mediaFileContext->getAvFormatContext();

    auto videoStreamIndex = get_video_stream_index(avFormatContext);
    if (videoStreamIndex < 0) {
        return nullptr;
    }

    AVCodecParameters *parameters = avFormatContext->streams[videoStreamIndex]->codecpar;

    auto *decoder = avcodec_find_decoder(parameters->codec_id);
    if (decoder != nullptr) {
        return new MediaFileFrameLoader(
                avFormatContext, parameters, decoder, videoStreamIndex, totalFramesToRead
        );
    }

    return nullptr;
}

MediaFileFrameLoader::MediaFileFrameLoader(
        AVFormatContext *avFormatContext,
        AVCodecParameters *parameters,
        const AVCodec *avVideoCodec,
        int videoStreamIndex,
        int totalFramesToRead
) : avFormatContext(avFormatContext),
    parameters(parameters),
    avVideoCodec(avVideoCodec),
    videoStreamIndex(videoStreamIndex),
    totalFramesToRead(totalFramesToRead) {}

bool MediaFileFrameLoader::loadFrame(jobject bitmap) {
    return false;
}
