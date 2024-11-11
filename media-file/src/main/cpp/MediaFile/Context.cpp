//
// Created by Oleksandr Berezhnyi on 18.10.2024.
//

#include <string>
#include <MediaFile/Context.hpp>

namespace MediaFile {
    Context::Context(AVFormatContext *avFormatContext) :
            avFormatContext(avFormatContext) {}

    Context::~Context() {
        avformat_close_input(&avFormatContext);
    }

    AVFormatContext *Context::getAvFormatContext() const {
        return avFormatContext;
    }

    Context *Context::open(const char *url) {
        AVFormatContext *avFormatContext = nullptr;
        if (avformat_open_input(&avFormatContext, url, nullptr, nullptr)) {
            return nullptr;
        }
        return new Context(avFormatContext);
    }

    Context *Context::open(
            int32_t file_descriptor,
            int64_t skip_initial_bytes,
            const char *formatNameHint
    ) {
        AVFormatContext *predefinedContext = avformat_alloc_context();

        if (skip_initial_bytes > 0) {
            predefinedContext->skip_initial_bytes = skip_initial_bytes;
        }

        if (formatNameHint != nullptr) {
            predefinedContext->iformat = av_find_input_format(formatNameHint);
        }

        AVDictionary *options = nullptr;
        av_dict_set(&options, "fd", std::to_string(file_descriptor).c_str(), 0);

        int result = avformat_open_input(&predefinedContext, "fd:", nullptr, &options);

        av_dict_free(&options);

        if (result) {
            avformat_free_context(predefinedContext);
            return nullptr;
        }

        return new Context(predefinedContext);
    }
}
