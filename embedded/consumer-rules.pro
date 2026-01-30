# Beyond Identity Embedded SDK ProGuard Rules

# Keep the main SDK singleton and all its public methods
-keep class com.beyondidentity.embedded.sdk.EmbeddedSdk {
    public *;
}

# Keep all public API model classes and their members
-keep class com.beyondidentity.embedded.sdk.models.** { *; }

# Keep exception classes
-keep class com.beyondidentity.embedded.sdk.exceptions.** { *; }

# Keep JavaResult utility class for Java interop
-keep class com.beyondidentity.embedded.sdk.utils.JavaResult { *; }

# Preserve Kotlin metadata for the SDK classes (needed for reflection and coroutines)
-keep class kotlin.Metadata { *; }

# Keep Kotlin coroutines-related classes that the SDK uses
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# Keep data class fields and methods (component functions, copy, etc.)
-keepclassmembers class com.beyondidentity.embedded.sdk.models.** {
    public <init>(...);
    public *** component*();
    public *** copy(...);
}

# Preserve line numbers for debugging stack traces
-keepattributes SourceFile,LineNumberTable

# Keep generic signatures for Kotlin types
-keepattributes Signature

# Keep annotations
-keepattributes *Annotation*

# Keep companion objects
-keepclassmembers class com.beyondidentity.embedded.sdk.** {
    public static *** Companion;
}

# Preserve sealed class hierarchies
-keep class * extends com.beyondidentity.embedded.sdk.models.RedeemOtpResponse
