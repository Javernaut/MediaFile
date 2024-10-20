//
// Created by Oleksandr Berezhnyi on 19.10.2024.
//

#ifndef WHATTHECODEC_MEDIAFILEUTILITY_H
#define WHATTHECODEC_MEDIAFILEUTILITY_H

namespace MediaFileUtility {
    void setAndroidContext(void *);

    void setJavaVM(JavaVM *vm);

    void reset();
}

#endif //WHATTHECODEC_MEDIAFILEUTILITY_H
