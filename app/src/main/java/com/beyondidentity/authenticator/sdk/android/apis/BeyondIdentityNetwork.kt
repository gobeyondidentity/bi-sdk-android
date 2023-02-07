package com.beyondidentity.authenticator.sdk.android.apis

import com.beyondidentity.authenticator.sdk.android.configs.BeyondIdentityConfig
import com.beyondidentity.authenticator.sdk.android.utils.PKCEUtil
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class AuthorizeResponse(
    @SerializedName("authenticate_url")
    val authenticateUrl: String?,
)

data class TokenResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("expires_in")
    val expiresIn: Int,
    @SerializedName("scope")
    val scope: String,
    @SerializedName("id_token")
    val idToken: String,
)

interface BeyondIdentityApiService {
    @GET(BeyondIdentityConfig.AUTHORIZATION_ENDPOINT)
    suspend fun authorize(
        @Query("client_id") client_id: String = BeyondIdentityConfig.CLIENT_ID,
        @Query("code_challenge") code_challenge: String = PKCEUtil.generateCodeChallenge(PKCEUtil.generateCodeVerifier()),
        @Query("code_challenge_method") code_challenge_method: String = "S256",
        @Query("redirect_uri") redirect_uri: String = BeyondIdentityConfig.REDIRECT_URI,
        @Query("response_type") response_type: String = "code",
        @Query("scope") scope: String = BeyondIdentityConfig.SCOPE,
        @Query("state") state: String = "state",
    ): Response<AuthorizeResponse>

    @FormUrlEncoded
    @POST(BeyondIdentityConfig.TOKEN_ENDPOINT)
    suspend fun token(
        @Field("client_id") client_id: String = BeyondIdentityConfig.CLIENT_ID,
        @Field("code") code: String = "code",
        @Field("code_verifier") code_verifier: String = PKCEUtil.generateCodeVerifier(),
        @Field("grant_type") grant_type: String = "authorization_code",
        @Field("redirect_uri") redirect_uri: String = BeyondIdentityConfig.REDIRECT_URI,
        @Field("state") state: String = "state",
    ): Response<TokenResponse>
}

object BeyondIdentityRetrofitBuilder {
    private fun getBeyondIdentityRetrofit() = Retrofit.Builder()
        .baseUrl("https://beyondidentity-cloud.byndid.com")
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { level = BODY })
                .build(),
        )
        .build()

    val BEYOND_IDENTITY_API_SERVICE: BeyondIdentityApiService by lazy {
        getBeyondIdentityRetrofit().create(
            BeyondIdentityApiService::class.java
        )
    }
}
