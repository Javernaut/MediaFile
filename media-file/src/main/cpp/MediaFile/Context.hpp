//
// Created by Oleksandr Berezhnyi on 18.10.2024.
//

#pragma once

extern "C" {
#include <libavformat/avformat.h>
}

namespace MediaFile {
    class Context {
    private:
        AVFormatContext *avFormatContext;
    public:
        // Takes ownership for the AVFormatContext
        explicit Context(AVFormatContext *avFormatContext);

        [[nodiscard]] AVFormatContext *getAvFormatContext() const;

        ~Context();

        static Context *open(const char *url);

        static Context *open(
                int32_t file_descriptor,
                int64_t skip_initial_bytes,
                const char *formatNameHint
        );
    };
}
