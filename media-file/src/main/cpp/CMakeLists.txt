cmake_minimum_required(VERSION 3.4.1)
project(MediaFile)

set(ffmpeg_dir ${CMAKE_SOURCE_DIR}/../../../../ffmpeg-android-maker/output)
set(ffmpeg_libs ${ffmpeg_dir}/lib/${ANDROID_ABI})

include_directories(
        # Headers of FFmpeg
        ${ffmpeg_dir}/include/${ANDROID_ABI}
        # Our own headers
        .
)

set(
        # List variable name
        ffmpeg_libs_names
        # Values in the list
        avutil avformat avcodec swscale)

foreach (ffmpeg_lib_name ${ffmpeg_libs_names})
    add_library(
            ${ffmpeg_lib_name}
            SHARED
            IMPORTED)
    set_target_properties(
            ${ffmpeg_lib_name}
            PROPERTIES
            IMPORTED_LOCATION
            ${ffmpeg_libs}/lib${ffmpeg_lib_name}.so)
endforeach ()

add_library(
        # Name for a library to build
        media-file
        # Type of a library
        SHARED
        # All cpp files to compile
        Main.cpp
        log/ffmpeg.cpp
        MediaFile/Context.cpp
        MediaFile/Builder.cpp
        MediaFile/MediaFileUtility.cpp
        MediaFile/FrameLoader.cpp
        jni/MediaFileFrameLoader_jni.cpp
        jni/MediaFileContext_jni.cpp
        jni/MediaFileFactory_jni.cpp
        jni/MediaSource_jni.cpp)

target_link_libraries(
        # Library to link
        media-file
        # List of libraries to link against:
        # Library for writing messages in LogCat
        log
        # Library for processing Bitmap objects
        jnigraphics
        # FFmpeg libraries
        ${ffmpeg_libs_names})
