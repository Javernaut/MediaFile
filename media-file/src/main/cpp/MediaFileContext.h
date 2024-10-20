//
// Created by Oleksandr Berezhnyi on 18.10.2024.
//

#ifndef WHATTHECODEC_MEDIAFILECONTEXT_H
#define WHATTHECODEC_MEDIAFILECONTEXT_H

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
};


#endif //WHATTHECODEC_MEDIAFILECONTEXT_H
