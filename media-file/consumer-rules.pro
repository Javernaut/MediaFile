# The MediaFileBuilder's methods are called from native code via a sort of refleciton api
-keep class io.github.javernaut.mediafile.MediaInfoBuilder {
    <methods>;
}

-keep class io.github.javernaut.mediafile.model.BasicStreamInfo