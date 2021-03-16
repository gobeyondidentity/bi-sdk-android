![Beyond-Identity-768x268](https://user-images.githubusercontent.com/6456218/111526630-5c826d00-8735-11eb-84ae-809af105b626.jpeg)

# Beyond Identity Android SDKs

## SDKs
### Authenticator
The authenticator SDK will be used in conjunction with the existing authenticator native clients  
Android, iOS, macOS, Windows, where most of the heavy lifting will be left up to them

### Embedded
WIP: This will be a holistic SDK solution for CIAM clients, offering the entire experience embedded in their
product.


## Setup
### Requirements
The Authenticator supports Android API 28 and above
### Authenticator
[![](https://jitpack.io/v/byndid/bi-sdk-android.svg)](https://jitpack.io/#byndid/bi-sdk-android)

The Authenticator SDK is avaiable on [Jitpack](https://jitpack.io)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
...
dependencies {
    implementation "com.beyondidentity.android.sdk:authenticator:x.x.x"
}
```
 
### Usage

Add `AuthView` to your sign in screen and configure it.

```kotlin
val authView = findViewById<AuthView>(R.id.auth_view)

/**
* ! WARNING !
* Custom URL Schemes offer a potential attack as Android allows any 
* URL Scheme to be claimed by multiple apps and thus malicious 
* apps can hijack sensitive data. 
*
* To mitigate this risk, use verified android app links
* https://developer.android.com/training/app-links/verify-site-associations
* 
* We're bringing PKCE support very soon to mitigate the custom url scheme risk.
*/
authView.initAuthView(
    // Url that kicks off OAUTH2
    loginUrl = "https://example.com/signin",
    // Register <intent-filter> for this url in order to intercept it once the authentication is successful
    // and extract the access token which will be used to make API requests
    redirectUrl = "https://example.com/redirect",
    // Custom action to handle sign ups for your product
    signupButtonListener = {
        Toast.makeText(
            context,
            "Sign up for Beyond Identity",
            Toast.LENGTH_SHORT
        ).show()
    }
)
```

## Sample App Walkthrough

## Prerequisites
Since most of the heavy lifting is delegated to the Beyond Identity Authenticator client when integrating the Authenticator SDK you need to follow the steps on your development phone or emulator (you'll need google play to download the app) to create an account with our demo app Acme Pay https://acme-app.byndid.com/
It should only take a minute.

