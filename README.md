# MediaFile

[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![MIT license](http://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/Javernaut/MediaFile/blob/main/LICENSE.txt)
[![Android Weekly #378](https://androidweekly.net/issues/issue-378/badge)](https://androidweekly.net/issues/issue-378)
[![Android Weekly #396](https://androidweekly.net/issues/issue-396/badge)](https://androidweekly.net/issues/issue-396)

## Overview

The library reads basic information about video and audio files.

For **video** streams:

* Video codec name
* Bit rate
* Frame rate and size

For **audio** streams:

* Audio codec name
* Bit rate
* Number of channels
* Channel layout
* Sample rate and format

For **subtitle** streams: title and language from stream’s metadata.

An incubating feature allows reading N equidistant frames from a video stream.

Supported ABIs are: **armeabi-v7a**, **arm64-v8a**, **x86** and **x86_64**.

## How it works

Under the hood this library uses [FFmpeg](https://ffmpeg.org/). Android-specific binaries of FFmpeg
are produced with [ffmpeg-android-maker](https://github.com/Javernaut/ffmpeg-android-maker).

Extensive description can be found
in [this article](https://proandroiddev.com/a-story-about-ffmpeg-in-android-part-ii-integration-55fb217251f0),
though the article was written at the time this library wasn't extracted as a separate repository.

FFmpeg's C API is accessed directly. Native part is connected to JVM part via JNI layer.

## APIs

`MediaFileFactory` class represents the entry point for `MediaFile` instances retrieval.

FFmpeg supports various types of input. MediaFile uses only a subset, that covers local content
accessing:

- __MediaSource.File()__. Despite the restrictions Android OS imposes on raw file paths usage, they
  are still valid if you
  want to use the files in your app's directories. Backed by _file_ protocol.

- __MediaSource.FileDescriptor()__. A `ParcelFileDescriptor` and `AssetFileDescriptor` are covered
  with this MediaSource type. Backed by _fd_ protocol.

- __MediaSource.Content()__. Regular `Uri`s with `content://` scheme can also be read by FFmpeg
  directly. Backed by _android_content_ protocol.

`MediaFile` and `FrameLoader` implement `AutoClosable`, so they can be used in try-with-resources (
Java) or with use() function (Kotlin).

Logs from FFmpeg can be redirected to LogCat, which is controlled by
`MediaFileFactory#setMinLogLevel()` method.

## Download

The library is available via Maven Central, so you are able to use it as a dependency:

```groovy
dependencies {
    implementation 'io.github.javernaut:mediafile:2.0.0'
}

repositories {
    mavenCentral()
}
```

## Build on your own

It is possible to have a different set of modules for the underlying FFmpeg's binaries. For that you
need to rebuild them yourself. In order to do that you have to
get [ffmpeg-android-maker](https://github.com/Javernaut/ffmpeg-android-maker) source code and
compile it. It is already added as a submodule. So the first thing to do is to load it:

`git submodule update --init`

Then you need to setup and execute the ffmpeg-android-maker's script. The command used to generate
the artifacts for Maven Central looks like this:

`./ffmpeg-android-maker.sh -dav1d`

More details about how to setup your environment for FFmpeg compilation can be found
in [ffmpeg-android-maker](https://github.com/Javernaut/ffmpeg-android-maker) repository.

## License

MediaFile library's source code is available under the MIT license. See
the [LICENSE.txt](https://github.com/Javernaut/MediaFile/blob/main/LICENSE.txt) file for more
details.
