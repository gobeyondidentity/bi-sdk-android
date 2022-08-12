package com.beyondidentity.authenticator.sdk.android.utils

import com.beyondidentity.authenticator.sdk.android.configs.OktaConfig
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

data class OktaV1TokenResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("expires_in")
    val expiresIn: Int,
    @SerializedName("scope")
    val scope: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("id_token")
    val idToken: String,
    @SerializedName("device_secret")
    val deviceSecret: String,
)

interface OktaApiService {
    @FormUrlEncoded
    @POST("v1/token")
    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun v1Token(
        @Field("client_id") clientId: String = OktaConfig.CLIENT_ID,
        @Field("code") code: String,
        @Field("code_verifier") codeVerifier: String,
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("redirect_uri") redirectUri: String = OktaConfig.REDIRECT_URI,
    ): Response<OktaV1TokenResponse>
}

object OktaRetrofitBuilder {
    private fun getOktaRetrofit() = Retrofit.Builder()
        .baseUrl("https://${OktaConfig.DOMAIN}/oauth2/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { level = BODY })
                .build(),
        )
        .build()

    val OKTA_API_SERVICE: OktaApiService by lazy { getOktaRetrofit().create(OktaApiService::class.java) }
}
