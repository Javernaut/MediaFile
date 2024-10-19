//
// Created by Oleksandr Berezhnyi on 14/10/19.
//

#include "frame_loader_context.h"
#include "utils.h"
#include "Reinterpret.h"

void frame_loader_context_free(int64_t handle) {
    auto *frameLoaderContext = Reinterpret::fromHandle<FrameLoaderContext>(handle);
    auto *avFormatContext = frameLoaderContext->avFormatContext;

    avformat_close_input(&avFormatContext);

    free(frameLoaderContext);
}
