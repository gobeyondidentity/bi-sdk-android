<p align="center">
   <br/>
   <a href="https://developers.beyondidentity.com" target="_blank"><img src="https://user-images.githubusercontent.com/238738/178780350-489309c5-8fae-4121-a20b-562e8025c0ee.png" width="150px" ></a>
   <h3 align="center">Beyond Identity</h3>
   <p align="center">Universal Passkeys for Developers</p>
   <p align="center">
   All devices. Any protocol. Zero shared secrets.
   </p>
</p>

# Beyond Identity Android SDK

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

### Embedded SDK

Goodbye, passwords! The Beyond Identity SDKs allow you to embed the Passwordless experience into your product. Users will not need to download the Beyond Identity Authenticator. These SDKs supports OIDC and OAuth2.

## Installation

### Gradle

To enable the retrieval of Cloudsmith hosted packages via Gradle, we need to add the Cloudsmith repository to
the `root/build.gradle` file.

```groovy
repositories {
    maven {
        url "https://packages.beyondidentity.com/public/bi-sdk-android/maven/"
    }
}
```

After the repository is added, we can specify the Beyond Identity dependencies.

```groovy
dependencies {
    implementation 'com.beyondidentity.android.sdk:embedded:[version]'
}
```

## Usage

Check out the [Developer Documentation](https://developer.beyondidentity.com) and the [SDK API Documentation](https://gobeyondidentity.github.io/bi-sdk-android/) for more information.

### Setup

First, before calling the Embedded functions, make sure to initialize the SDK.

```kotlin
import com.beyondidentity.embedded.sdk.EmbeddedSdk

EmbeddedSdk.init(
    app: Application,
    keyguardPrompt: (((allow: Boolean, exception: Exception?) -> Unit) -> Unit)?,
    logger: (String) -> Unit,
    biometricAskPrompt: String, /* Optional */
    allowedDomains: List<String>?, /* Optional */
)
```
