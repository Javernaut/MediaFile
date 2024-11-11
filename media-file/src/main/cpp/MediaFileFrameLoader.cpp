//
// Created by Oleksandr Berezhnyi on 20.10.2024.
//

#include "MediaFileFrameLoader.hpp"
#include <android/bitmap.h>

extern "C" {
#include <libavformat/avformat.h>
#include <libavcodec/avcodec.h>
#include <libswscale/swscale.h>
#include <libavutil/imgutils.h>
}

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

static bool frame_extractor_load_frame(MediaFileFrameLoader *mediaFileFrameLoader, JNIEnv *env,
                                       jobject jBitmap) {
    AndroidBitmapInfo bitmapMetricInfo;
    AndroidBitmap_getInfo(env, jBitmap, &bitmapMetricInfo);

    auto pixelFormat = static_cast<AVPixelFormat>(mediaFileFrameLoader->parameters->format);
    if (pixelFormat == AV_PIX_FMT_NONE) {
        // With pipe protocol some files fail to provide pixel format info.
        // In this case we can't establish neither scaling nor even a frame extracting.
        return false;
    }

    bool resultValue = true;

    SwsContext *scalingContext =
            sws_getContext(
                    // srcW
                    mediaFileFrameLoader->parameters->width,
                    // srcH
                    mediaFileFrameLoader->parameters->height,
                    // srcFormat
                    pixelFormat,
                    // dstW
                    bitmapMetricInfo.width,
                    // dstH
                    bitmapMetricInfo.height,
                    // dstFormat
                    AV_PIX_FMT_RGBA,
                    SWS_BICUBIC, nullptr, nullptr, nullptr);

    AVStream *avVideoStream = mediaFileFrameLoader->
            avFormatContext->
            streams[mediaFileFrameLoader->videoStreamIndex];

    int64_t videoDuration = avVideoStream->duration;

    // In some cases the duration is of a video stream is set to Long.MIN_VALUE and we need compute it in another way
    if (videoDuration == LONG_LONG_MIN && avVideoStream->time_base.den != 0) {
        videoDuration =
                mediaFileFrameLoader->avFormatContext->duration / avVideoStream->time_base.den;
    }

    // We extract frames right from the middle of a region, so the offset equals to a half of a region
    int64_t offset = videoDuration / mediaFileFrameLoader->totalFramesToRead / 2;

    AVPacket *packet = av_packet_alloc();
    AVFrame *frame = av_frame_alloc();

    int index = mediaFileFrameLoader->frameToRead++;

    int64_t seekPosition = videoDuration / mediaFileFrameLoader->totalFramesToRead * index + offset;
    av_seek_frame(mediaFileFrameLoader->avFormatContext,
                  mediaFileFrameLoader->videoStreamIndex,
                  seekPosition,
                  0);

    AVCodecContext *videoCodecContext = avcodec_alloc_context3(mediaFileFrameLoader->avVideoCodec);
    avcodec_parameters_to_context(videoCodecContext, mediaFileFrameLoader->parameters);
    avcodec_open2(videoCodecContext, mediaFileFrameLoader->avVideoCodec, nullptr);

    while (true) {
        if (av_read_frame(mediaFileFrameLoader->avFormatContext, packet) < 0) {
            // Couldn't read a packet, so we skip the whole frame
            resultValue = false;
            break;
        }

        if (packet->stream_index == mediaFileFrameLoader->videoStreamIndex) {
            avcodec_send_packet(videoCodecContext, packet);
            int response = avcodec_receive_frame(videoCodecContext, frame);
            if (response == AVERROR(EAGAIN)) {
                // A frame can be split across several packets, so continue reading in this case
                continue;
            }

            if (response >= 0) {
                AVFrame *frameForDrawing = av_frame_alloc();
                void *bitmapBuffer;
                AndroidBitmap_lockPixels(env, jBitmap, &bitmapBuffer);

                // Prepare a FFmpeg's frame to use Android Bitmap's buffer
                av_image_fill_arrays(
                        frameForDrawing->data,
                        frameForDrawing->linesize,
                        static_cast<const uint8_t *>(bitmapBuffer),
                        AV_PIX_FMT_RGBA,
                        bitmapMetricInfo.width,
                        bitmapMetricInfo.height,
                        1);

                // Scale the frame that was read from the media to a frame that wraps Android Bitmap's buffer
                sws_scale(
                        scalingContext,
                        frame->data,
                        frame->linesize,
                        0,
                        mediaFileFrameLoader->parameters->height,
                        frameForDrawing->data,
                        frameForDrawing->linesize);

                av_frame_free(&frameForDrawing);

                AndroidBitmap_unlockPixels(env, jBitmap);
                break;
            }
        }
        av_packet_unref(packet);
    }

    av_packet_free(&packet);
    av_frame_free(&frame);
    avcodec_free_context(&videoCodecContext);

    sws_freeContext(scalingContext);

    return resultValue;
}


bool MediaFileFrameLoader::loadFrame(JNIEnv *env, jobject bitmap) {
    return frame_extractor_load_frame(this, env, bitmap);
}
