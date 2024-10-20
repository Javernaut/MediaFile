//
// Created by Oleksandr Berezhnyi on 18.10.2024.
//

#include "MediaFileContext.h"

MediaFileContext::MediaFileContext(AVFormatContext *avFormatContext) :
        avFormatContext(avFormatContext) {}

MediaFileContext::~MediaFileContext() {
    avformat_close_input(&avFormatContext);
}

MediaFileContext *MediaFileContext::open(const char *url) {
    AVFormatContext *context = nullptr;
    if (avformat_open_input(&context, url, nullptr, nullptr)) {
        return nullptr;
    }
    return new MediaFileContext(context);
}

AVFormatContext *MediaFileContext::getAvFormatContext() const {
    return avFormatContext;
}
