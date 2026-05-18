# R8/Proguard configuration from com.google.firebase:firebase-components:19.0.0 to fix crash on MLKit init
-keep class * implements com.google.firebase.components.ComponentRegistrar { void <init>(); }
-keep,allowshrinking interface com.google.firebase.components.ComponentRegistrar