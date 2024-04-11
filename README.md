# MediaFile

[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![MIT license](http://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/Javernaut/MediaFile/blob/main/LICENSE.txt)
[![Android Weekly #378](https://androidweekly.net/issues/issue-378/badge)](https://androidweekly.net/issues/issue-378)
[![Android Weekly #396](https://androidweekly.net/issues/issue-396/badge)](https://androidweekly.net/issues/issue-396)

The library reads basic information about video and audio files.

For **video** streams:
* Video codec name
* Bit rate
* Frame size
* 4 equidistant frames can be read (incubating feature)

For **audio** streams:
* Audio codec name
* Bit rate
* Number of channels
* Channel layout
* Sample rate and format

For **subtitle** streams: title and language from stream’s metadata.

Supported ABIs are: **armeabi-v7a**, **arm64-v8a**, **x86** and **x86_64**.

The main purpose is to show how to use the output of [ffmpeg-android-maker](https://github.com/Javernaut/ffmpeg-android-maker).

Extensive description can be found in [this article](https://proandroiddev.com/a-story-about-ffmpeg-in-android-part-ii-integration-55fb217251f0), though the article was written at the time this library wasn't extracted as a separate repository.

## How it works

The codebase has a native part that glues FFmpeg libs to JVM part.  

FFmpeg accepts 2 types of input: File paths and File Descriptors. **File paths** are better and allow the FFmpeg to use all the functionality it has. **File Descriptors** can be passed to FFmpeg via pipe protocol, but it has certain [problems](https://ffmpeg.org/ffmpeg-protocols.html#pipe) like inability to seek backward. That is why the number of frames loaded is limited to 4 only. Though the File protocol doesn't have this problem.

Library tries to recreate a raw file path from a Uri and pass it to FFmpeg. If it doesn't succeed, it falls back to File Descriptor way.

## Using

The library is available via Maven Central, so you are able to use it as a dependency:

```groovy
dependencies {
    implementation 'io.github.javernaut:mediafile:x.y.z'
}

repositories {
    // ...
    mavenCentral()
}
```

## Build on your own

In order to compile the library yourself, you have to get [ffmpeg-android-maker](https://github.com/Javernaut/ffmpeg-android-maker) source code and compile it. It is already added as a submodule. So the first thing to do is to load it:

`git submodule update --init`  

Then you need to setup and execute the ffmpeg-android-maker's script. The command used to generate the artifacts for Maven Central looks like this:

`./ffmpeg-android-maker.sh -dav1d`

More details about how to setup your environment for FFmpeg compilation can be found in [ffmpeg-android-maker](https://github.com/Javernaut/ffmpeg-android-maker) repository.

## License

MediaFile library's source code is available under the MIT license. See the [LICENSE.txt](https://github.com/Javernaut/MediaFile/blob/main/LICENSE.txt) file for more details.
