//
// Created by Oleksandr Berezhnyi on 18.10.2024.
//

#include "MediaFileContext.h"

MediaFileContext::MediaFileContext(AVFormatContext *avFormatContext) :
        avFormatContext(avFormatContext) {}

MediaFileContext::~MediaFileContext() {
    avformat_close_input(&avFormatContext);
}

static MediaFileContext *openMediaFileContext(const char *url, AVFormatContext *predefinedContext) {
    if (avformat_open_input(&predefinedContext, url, nullptr, nullptr)) {
        return nullptr;
    }
    return new MediaFileContext(predefinedContext);
}

MediaFileContext *MediaFileContext::open(const char *url) {
    return openMediaFileContext(url, nullptr);
}

MediaFileContext *MediaFileContext::open(
        const char *url,
        long long skip_initial_bytes,
        const char *formatNameHint
) {
    AVFormatContext *predefinedContext = avformat_alloc_context();
    predefinedContext->skip_initial_bytes = skip_initial_bytes;
    predefinedContext->iformat = av_find_input_format(formatNameHint);
    auto result = openMediaFileContext(url, predefinedContext);
    if (result == nullptr) {
        avformat_free_context(predefinedContext);
    }
    return result;
}

AVFormatContext *MediaFileContext::getAvFormatContext() const {
    return avFormatContext;
}
