![Beyond-Identity-768x268](https://user-images.githubusercontent.com/6456218/111526630-5c826d00-8735-11eb-84ae-809af105b626.jpeg)

# Beyond Identity Android SDKs

## SDKs
### Authenticator
The authenticator SDK is meant to be used in conjunction with the existing Beyond Identity native apps  
(Android, iOS, macOS, Windows), where most of the heavy lifting will be left up to them.

### Embedded
WIP: This will be a holistic SDK solution for CIAM clients, offering the entire experience embedded in their
product.

## Setup
### Requirements
The Authenticator supports Android API 28 and above
### Authenticator

The Authenticator SDK is avaiable on [![](https://jitpack.io/v/byndid/bi-sdk-android.svg)](https://jitpack.io/#byndid/bi-sdk-android)

```groovy
// root build.gradle
repositories {
    maven { url 'https://jitpack.io' }
}
// ...
// module build.gradle
dependencies {
    implementation "com.github.byndid.bi-sdk-android:authenticator:x.x.x"
}
```
 
### Usage

#### Step 1.
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
    redirectUrl = "https://example.com/oauth2redirect",
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

#### Step 2
Register `<intent-filter>` for the redirect url.

```kotlin
<activity android:name=".MyOauth2RedirectReceiver">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />

        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <!-- WARNING! Use verified app links (https://yourdomain) vs deep links, as deep -->
        <!-- links can be intercepted by other apps -->
        <data
            android:scheme="https"
            android:host="example.com"
            android:path="oauth2redirect"/>
    </intent-filter>
</activity>
```

##### Step 3
Once the authentication is successfull, you can get the access token from the `Intent` `Uri`
```kotlin
val accessToken = intent.data?.getQueryParameter("access_token") // replace access_token param key with the key your backend sets

// You can start using the access token to make requests to your backend
apiService.getUsers(accessToken)
```

## Sample App Walkthrough

## Prerequisites
Since most of the heavy lifting is delegated to the Beyond Identity Authenticator client when integrating the Authenticator SDK you need to follow the steps on your development phone or emulator (you'll need google play to download the app) to create an account with our demo app Acme Pay https://acme-app.byndid.com/
It should only take a minute.

Now that you have the Beyond Identity app setup with a profile for Acme app, go ahead and run the app.

Once you click sign in, the Oauth2 process is initated on the Acme backend
```kotlin
// endpoint to kickoff oauth2 | where to redirect once auth is done
$ACME_CLOUD_URL/start?redirect=my-app://oauth2redirect
```

The Acme app backend will complete the Oauth2 flow and once it gets the access token it will launch the redirect uri with access token
```kotlin
my-app://oauth2redirect?access_token=[your_access_token]
```
Which will be intercepted by `MyOauth2RedirectReceiver` and saved for further usage. At that point, you're able to use it to make 
API requests to the Acme backend.