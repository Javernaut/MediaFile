-keep public class io.github.javernaut.mediafile.** {
    public *;
}

# The MediaFileBuilder's methods are called from native code via a sort of refleciton api
-keep class io.github.javernaut.mediafile.MediaFileBuilder {
    <methods>;
}
