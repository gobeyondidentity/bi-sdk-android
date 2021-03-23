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

Currently, Beyond Identity only supports clients with a backend. This allows for two flows:

1. [Confidential Client Handles Everything](#Confidential-Client-Handles-Everything)  
   Your confidential client is configured to handle getting the authorization code and exchanging that code for an access token and id token.

    <img width="1024" alt="Screen Shot 2021-03-24 at 4 18 26 PM" src="https://user-images.githubusercontent.com/6456218/112377859-9b2ba080-8cbc-11eb-95f0-a0f32973da68.png">

2. [Public Client And Confidential Client](#Public-Client-And-Confidential-Client)  
   The public client queries for the authorization code and your confidential client makes the token exchange.

   <img width="1024" alt="Screen Shot 2021-03-24 at 4 18 14 PM" src="https://user-images.githubusercontent.com/6456218/112377947-b39bbb00-8cbc-11eb-8b08-89b68e5b20c2.png">

For either flow you'll want to create and add an `AuthView` in your `Activity/Fragment`. This view contains both Beyond Identity Sign In and Sign Up buttons.
When the user taps the "Sign In" button, your session will start. When the user taps "Sign Up" button the AuthView will trigger your sign up action.

```kotlin
val authView = findViewById<AuthView>(R.id.auth_view)

authView.initAuthView(
    signInUri: Uri,
    signUpOnClickListener: View.OnClickListener,
)
```

> :warning: WARNING :warning:
> Custom URL Schemes offer a potential attack as Android allows any URL Scheme to
> be claimed by multiple apps and thus malicious apps can hijack sensitive data.
> To mitigate this risk, use verified android app links https://developer.android.com/training/app-links/verify-site-associations

#### Confidential Client Handles Everything

In this scenerio your confidential client (backend) handles getting the authorization code and exchanging that for an access token and id token.

It is up to the developer to decide how to invoke their confidential client in order to initiate the flow, as well as what the response would be. As an example, a response could be a session id associated with the authenticated user or the userinfo payload that was queried on the confidential client.

Build a `Uri` that points to the authentication webpage that you have configured in your confidential client. A secure, embedded web view loads and displays the page, from which the user can authenticate.

```kotlin
val signInUri = Uri.parse("https://example.com/signin").buildUpon().apply {
        // ! WARNING ! Don't use custom schemes (myapp://redirect), Use verified app links for your domain (https://yourdomain.com)
        appendQueryParameter("redirect", "https://example.com/oauth2redirect")
    }.build()
```

On completion, the service sends a callback `url` to the registered redirect with an authentication token or anything else you have configured your confidential client to send in that url. In order to intercept that callback, you need to register an `<intent-filter>`.

```xml
<activity android:name=".MyOauth2RedirectReceiver">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />

        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data
            android:scheme="https"
            android:host="example.com"
            android:path="oauth2redirect"/>
    </intent-filter>
</activity>
```

Finally, you'll be able to get the response from the incoming `Intent` in the registered `<intent-filter>` Activity

```kotlin
val session = intent.data.getQueryParameter("myparamkey")
```

For more details on configuring your confidential client, see the [Developer Docs](https://docs.byndid.com).

#### Public Client And Confidential Client

In this scenerio your public client (mobile app) handles getting the authorization code and your confidential client (backend) handles exchanging that code for an access token and id token.

It is up to the developer to decide what the response would be for your confidential client. As an example, a response could be a session id associated with the authenticated user or the userinfo payload that was queried on the confidential client.

The Beyond Identity `/authorize` endpoint supports the following parameters:

- `client_id`: unique ID for a tenant's registered OIDC client.
- `redirect_uri`: must match one of the configured values for the client.
- `response_type`: Only `code` supported at this time.
- `scope`: must contain `openid` (except for OAuth2).
- `state`: random string that the client can use to maintain state between the request and the callback.

Build a `Uri` that points to Beyond Identity's `/authorize` endpoint with the appropriate parameters. A secure, embedded web view loads and displays the page, from which the user can authenticate.

```kotlin
val state = UUID.randomUUID().toString()

val uri = Uri.parse(authorizationEndpoint).buildUpon().apply {
    appendQueryParameter("client_id", clientId)
    appendQueryParameter("redirect_uri", redirectUri)
    appendQueryParameter("response_type", responseType)
    appendQueryParameter("scope", scope)
    appendQueryParameter("state", state)
}.build()
```

On completion, the service sends a callback `url` with state and authorization token parameters. In order to intercept that callback, you need to register an `<intent-filter>`.

```xml
<activity android:name=".MyOauth2RedirectReceiver">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />

        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data
            android:scheme="https"
            android:host="example.com"
            android:path="oauth2redirect"/>
    </intent-filter>
</activity>
```

Finally, you'll be able to get the response from the incoming `Intent` in the registered `<intent-filter>` Activity

```kotlin
// Check the state matches the state you've sent
val stateParam = intent.data.getQueryParameter("state")
if (state != stateParam) {
    // someone is interfering with our auth process
}

// Exchange the authorization code for access token and id token
val authorizationCode = intent.data.getQueryParameter("code")
myService.exchangeCodeForToken(authorizationCode) { response
    OnSuccess -> response
    OnError -> Throwable("Unable to sign in")
}
```

For more details on configuring your backend, see the [Developer Docs](https://docs.byndid.com).

## Sample App Walkthrough

## Prerequisites

Since most of the heavy lifting is delegated to the Beyond Identity Authenticator client when integrating the Authenticator SDK you need to follow the steps on your development phone or emulator (you'll need google play to download the app) to create an account with our demo app Acme Pay https://acme-app.byndid.com/
It should only take a minute.

Now that you have the Beyond Identity app setup with a profile for Acme app, go ahead and run the app.

Once you click sign in, the Oauth2 process is initated on the Acme backend

```kotlin
// endpoint to kickoff oauth2 | where to redirect once auth is done
$ACME_CLOUD_URL/start?redirect=acme-app://oauth2redirect
```

The Acme app backend will complete the Oauth2 flow and once it gets the access token it will launch the redirect uri with access token

```kotlin
acme-app://oauth2redirect?session=[session]
```

Which will be intercepted by `MyOauth2RedirectReceiver` and saved for further usage. At that point, you're able to use it to make
API requests to the Acme backend.
