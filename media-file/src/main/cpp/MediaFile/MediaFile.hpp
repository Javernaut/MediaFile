//
// Created by Oleksandr Berezhnyi on 19.10.2024.
//

#pragma once

namespace MediaFile {
    void setAndroidContext(void *);

    void init(JavaVM *vm);

    void reset();
}
