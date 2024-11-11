//
// Created by Oleksandr Berezhnyi on 20.10.2024.
//

#include "MediaFileBuilder.h"
#include "log/log.h"

extern "C" {
#include <libavformat/avformat.h>
#include <libavcodec/avcodec.h>
#include <libavutil/bprint.h>
#include <libavutil/channel_layout.h>
}

// TODO Consider MediaFileUtility -> MediaFileFacade, and moving the reflection related code to new MediaFileBuilderUtility
static struct fields {
    struct {
        jclass clazz;
        jmethodID onMediaFileFoundID;
        jmethodID onVideoStreamFoundID;
        jmethodID onAudioStreamFoundID;
        jmethodID onSubtitleStreamFoundID;
        jmethodID onErrorID;
        jmethodID createBasicInfoID;
    } MediaFileBuilder;
} fields;

static JavaVM *javaVM;

static JNIEnv *utils_get_env() {
    JNIEnv *env;
    if (javaVM->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return nullptr;
    }
    return env;
}

static int utils_fields_init(JavaVM *vm) {
    javaVM = vm;

    JNIEnv *env = utils_get_env();
    if (env == nullptr) {
        return -1;
    }

#define GET_CLASS(clazz, str, b_globlal) do { \
    (clazz) = env->FindClass((str)); \
    if (!(clazz)) { \
        LOGE("FindClass(%s) failed", (str)); \
        return -1; \
    } \
    if (b_globlal) { \
        (clazz) = (jclass) env->NewGlobalRef((clazz)); \
        if (!(clazz)) { \
            LOGE("NewGlobalRef(%s) failed", (str)); \
            return -1; \
        } \
    } \
} while (0)

#define GET_ID(get, id, clazz, str, args) do { \
    (id) = env->get((clazz), (str), (args)); \
    if (!(id)) { \
        LOGE(#get"(%s) failed", (str)); \
        return -1; \
    } \
} while (0)

    // Actual work

    GET_CLASS(fields.MediaFileBuilder.clazz,
              "io/github/javernaut/mediafile/factory/MediaFileBuilder", true);

    GET_ID(GetMethodID,
           fields.MediaFileBuilder.onErrorID,
           fields.MediaFileBuilder.clazz,
           "onError", "()V");

    GET_ID(GetMethodID,
           fields.MediaFileBuilder.createBasicInfoID,
           fields.MediaFileBuilder.clazz,
           "createBasicInfo",
           "(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;I)Lio/github/javernaut/mediafile/BasicStreamInfo;");

    GET_ID(GetMethodID,
           fields.MediaFileBuilder.onMediaFileFoundID,
           fields.MediaFileBuilder.clazz,
           "onMediaFileFound", "(Ljava/lang/String;)V");

    GET_ID(GetMethodID,
           fields.MediaFileBuilder.onVideoStreamFoundID,
           fields.MediaFileBuilder.clazz,
           "onVideoStreamFound", "(Lio/github/javernaut/mediafile/BasicStreamInfo;JDII)V");

    GET_ID(GetMethodID,
           fields.MediaFileBuilder.onAudioStreamFoundID,
           fields.MediaFileBuilder.clazz,
           "onAudioStreamFound",
           "(Lio/github/javernaut/mediafile/BasicStreamInfo;JLjava/lang/String;IILjava/lang/String;)V");

    GET_ID(GetMethodID,
           fields.MediaFileBuilder.onSubtitleStreamFoundID,
           fields.MediaFileBuilder.clazz,
           "onSubtitleStreamFound", "(Lio/github/javernaut/mediafile/BasicStreamInfo;)V");

    return 0;
}

static void utils_fields_free() {
    JNIEnv *env = utils_get_env();
    if (env == nullptr) {
        return;
    }

    env->DeleteGlobalRef(fields.MediaFileBuilder.clazz);

    javaVM = nullptr;
}

static void utils_call_instance_method_void(jobject instance, jmethodID methodID, ...) {
    va_list args;
    va_start(args, methodID);
    utils_get_env()->CallVoidMethodV(instance, methodID, args);
    va_end(args);
}

static jobject utils_call_instance_method_result(jobject instance, jmethodID methodID, ...) {
    va_list args;
    va_start(args, methodID);
    jobject result = utils_get_env()->CallObjectMethodV(instance, methodID, args);
    va_end(args);
    return result;
}

static jstring toJString(const char *cString) {
    jstring result = nullptr;
    if (cString != nullptr) {
        result = utils_get_env()->NewStringUTF(cString);
    }
    return result;
}

static jstring get_string(AVDictionary *metadata, const char *key) {
    jstring result = nullptr;
    AVDictionaryEntry *tag = av_dict_get(metadata, key, nullptr, 0);
    if (tag != nullptr) {
        result = utils_get_env()->NewStringUTF(tag->value);
    }
    return result;
}

static jstring get_title(AVDictionary *metadata) {
    return get_string(metadata, "title");
}

static jstring get_language(AVDictionary *metadata) {
    return get_string(metadata, "language");
}

static jobject createBasicStreamInfo(jobject jMediaFileBuilder,
                                     AVFormatContext *avFormatContext,
                                     int index) {

    AVStream *stream = avFormatContext->streams[index];
    AVCodecParameters *parameters = stream->codecpar;

    auto codecDescriptor = avcodec_descriptor_get(parameters->codec_id);
    jstring jCodecName = utils_get_env()->NewStringUTF(codecDescriptor->long_name);

    return utils_call_instance_method_result(jMediaFileBuilder,
                                             fields.MediaFileBuilder.createBasicInfoID,
                                             index,
                                             get_title(stream->metadata),
                                             jCodecName,
                                             get_language(stream->metadata),
                                             stream->disposition);
}

static void onError(jobject jMediaFileBuilder) {
    utils_call_instance_method_void(jMediaFileBuilder,
                                    fields.MediaFileBuilder.onErrorID);
}

static void onMediaFileFound(jobject jMediaFileBuilder, AVFormatContext *avFormatContext) {
    const char *fileFormatName = avFormatContext->iformat->long_name;

    jstring jFileFormatName = utils_get_env()->NewStringUTF(fileFormatName);

    utils_call_instance_method_void(jMediaFileBuilder,
                                    fields.MediaFileBuilder.onMediaFileFoundID,
                                    jFileFormatName);
}

static void onVideoStreamFound(jobject jMediaFileBuilder,
                               AVFormatContext *avFormatContext,
                               int index) {
    AVCodecParameters *parameters = avFormatContext->streams[index]->codecpar;

    jobject jBasicStreamInfo = createBasicStreamInfo(jMediaFileBuilder, avFormatContext, index);

    AVRational guessedFrameRate = av_guess_frame_rate(
            avFormatContext,
            avFormatContext->streams[index],
            nullptr
    );

    double resultFrameRate =
            guessedFrameRate.den == 0 ? 0.0 :
            guessedFrameRate.num / (double) guessedFrameRate.den;

    utils_call_instance_method_void(jMediaFileBuilder,
                                    fields.MediaFileBuilder.onVideoStreamFoundID,
                                    jBasicStreamInfo,
                                    parameters->bit_rate,
                                    resultFrameRate,
                                    parameters->width,
                                    parameters->height);
}

static void onAudioStreamFound(jobject jMediaFileBuilder,
                               AVFormatContext *avFormatContext,
                               int index) {
    AVStream *stream = avFormatContext->streams[index];
    AVCodecParameters *parameters = stream->codecpar;

    jobject jBasicStreamInfo = createBasicStreamInfo(jMediaFileBuilder, avFormatContext, index);

    auto avSampleFormat = static_cast<AVSampleFormat>(parameters->format);
    auto jSampleFormat = toJString(av_get_sample_fmt_name(avSampleFormat));

    jstring jChannelLayout = nullptr;
    AVBPrint printBuffer;
    av_bprint_init(&printBuffer, 1, AV_BPRINT_SIZE_UNLIMITED);
    av_bprint_clear(&printBuffer);
    if (!av_channel_layout_describe_bprint(&parameters->ch_layout, &printBuffer)) {
        jChannelLayout = toJString(printBuffer.str);
    }
    av_bprint_finalize(&printBuffer, nullptr);

    utils_call_instance_method_void(jMediaFileBuilder,
                                    fields.MediaFileBuilder.onAudioStreamFoundID,
                                    jBasicStreamInfo,
                                    parameters->bit_rate,
                                    jSampleFormat,
                                    parameters->sample_rate,
                                    parameters->ch_layout.nb_channels,
                                    jChannelLayout);
}

static void onSubtitleStreamFound(jobject jMediaFileBuilder,
                                  AVFormatContext *avFormatContext,
                                  int index) {

    jobject jBasicStreamInfo = createBasicStreamInfo(jMediaFileBuilder, avFormatContext, index);

    utils_call_instance_method_void(jMediaFileBuilder,
                                    fields.MediaFileBuilder.onSubtitleStreamFoundID,
                                    jBasicStreamInfo);
}

static int STREAM_VIDEO = 1;
static int STREAM_AUDIO = 1 << 1;
static int STREAM_SUBTITLE = 1 << 2;

void MediaFileBuilder::init(JavaVM *vm) {
    utils_fields_init(vm);
}

void MediaFileBuilder::reset() {
    utils_fields_free();
}

void MediaFileBuilder::readMetaInfo(
        MediaFileContext *mediaFileContext,
        jobject jMediaFileBuilder,
        jint mediaStreamsMask
) {
    auto avFormatContext = mediaFileContext->getAvFormatContext();

    if (avformat_find_stream_info(avFormatContext, nullptr) < 0) {
        onError(jMediaFileBuilder);
        return;
    };

    onMediaFileFound(jMediaFileBuilder, avFormatContext);

    for (int pos = 0; pos < avFormatContext->nb_streams; pos++) {
        AVCodecParameters *parameters = avFormatContext->streams[pos]->codecpar;
        AVMediaType type = parameters->codec_type;
        switch (type) {
            case AVMEDIA_TYPE_VIDEO:
                if (mediaStreamsMask & STREAM_VIDEO) {
                    onVideoStreamFound(jMediaFileBuilder, avFormatContext, pos);
                }
                break;
            case AVMEDIA_TYPE_AUDIO:
                if (mediaStreamsMask & STREAM_AUDIO) {
                    onAudioStreamFound(jMediaFileBuilder, avFormatContext, pos);
                }
                break;
            case AVMEDIA_TYPE_SUBTITLE:
                if (mediaStreamsMask & STREAM_SUBTITLE) {
                    onSubtitleStreamFound(jMediaFileBuilder, avFormatContext, pos);
                }
                break;
            default:
                break;
        }
    }
}
