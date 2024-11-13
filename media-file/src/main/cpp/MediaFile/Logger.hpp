//
// Created by Oleksandr Berezhnyi on 12.11.2024.
//

#pragma once

namespace MediaFile::Logger {
    enum Level {
        QUITE = 0,
        FATAL,
        ERROR,
        WARN,
        VERBOSE,
        DEBUG
    };

    void setMinLevel(Level level);

    void init();
}
