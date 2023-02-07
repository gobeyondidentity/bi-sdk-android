package com.beyondidentity.authenticator.sdk.android.apis

import com.beyondidentity.authenticator.sdk.android.configs.Auth0Config
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

data class Auth0TokenResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("id_token")
    val idToken: String,
    @SerializedName("scope")
    val scope: String,
    @SerializedName("expires_in")
    val expiresIn: Int,
    @SerializedName("token_type")
    val tokenType: String,
)

interface Auth0ApiService {
    @FormUrlEncoded
    @POST("token")
    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun token(
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("client_id") clientId: String = Auth0Config.CLIENT_ID,
        @Field("code") code: String,
        @Field("code_verifier") codeVerifier: String,
        @Field("redirect_uri") redirectUri: String = Auth0Config.REDIRECT_URI,
    ): Response<Auth0TokenResponse>
}

object Auth0RetrofitBuilder {
    private fun getAuth0Retrofit() = Retrofit.Builder()
        .baseUrl("https://${Auth0Config.DOMAIN}/oauth/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { level = BODY })
                .build(),
        )
        .build()

    val AUTH0_API_SERVICE: Auth0ApiService by lazy { getAuth0Retrofit().create(Auth0ApiService::class.java) }
}
