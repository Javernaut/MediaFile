//
// Created by Oleksandr Berezhnyi on 18.10.2024.
//

#pragma once

extern "C" {
#include <libavformat/avformat.h>
}

class MediaFileContext {
private:
    AVFormatContext *avFormatContext;
public:
    // Takes ownership for the AVFormatContext
    explicit MediaFileContext(AVFormatContext *avFormatContext);

    AVFormatContext *getAvFormatContext() const;

    ~MediaFileContext();

    static MediaFileContext *open(const char *url);

    static MediaFileContext *open(
            int32_t file_descriptor,
            int64_t skip_initial_bytes,
            const char *formatNameHint
    );
};
