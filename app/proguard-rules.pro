# Add project specific ProGuard rules here.
-keepattributes *Annotation*

# Keep Room entities
-keep class com.retro.musicplayer.data.model.** { *; }

# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# ExoPlayer
-keep class androidx.media3.** { *; }
