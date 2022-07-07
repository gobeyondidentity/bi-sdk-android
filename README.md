![beyond-identity-logo](https://user-images.githubusercontent.com/6578679/172954923-7a0c741a-8ee6-4ba3-a610-1b073f3eec59.png)

# Beyond Identity Android SDKs

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

### [Embedded](wiki/embedded/getting_started)

The Embedded SDK is a holistic SDK solution offering the entire experience embedded in your product. Users will not need
to download the Beyond Identity Authenticator.

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
    implementation 'com.beyondidentity.android.sdk:[embedded|embedded-ui]:[version]'
}
```

## Usage
Check out the [documentation](https://developer.beyondidentity.com) for more information.
