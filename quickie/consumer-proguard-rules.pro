# R8/Proguard rules from com.google.firebase:firebase-components:19.0.1 to fix runtime crash on MLKit init when using
# strict full mode for keep rules (strictFullModeForKeepRules = true is default in AGP9+) since this mode no longer
# implicitly keeps the default constructor when a class is kept.
-keep class * implements com.google.firebase.components.ComponentRegistrar { void <init>(); }
-keep,allowshrinking interface com.google.firebase.components.ComponentRegistrar