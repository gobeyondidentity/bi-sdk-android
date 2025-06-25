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
    val authenticateUrl: String?
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
    val idToken: String
)

interface BeyondIdentityApiService {
    @GET(BeyondIdentityConfig.AUTHORIZATION_ENDPOINT)
    suspend fun authorize(
        @Query("client_id") clientId: String = BeyondIdentityConfig.CLIENT_ID,
        @Query("code_challenge") codeChallenge: String =
            PKCEUtil.generateCodeChallenge(PKCEUtil.generateCodeVerifier()),
        @Query("code_challenge_method") codeChallengeMethod: String = "S256",
        @Query("redirect_uri") redirectUri: String = BeyondIdentityConfig.REDIRECT_URI,
        @Query("response_type") responseType: String = "code",
        @Query("scope") scope: String = BeyondIdentityConfig.SCOPE,
        @Query("state") state: String = "state"
    ): Response<AuthorizeResponse>

    @FormUrlEncoded
    @POST(BeyondIdentityConfig.TOKEN_ENDPOINT)
    suspend fun token(
        @Field("client_id") clientId: String = BeyondIdentityConfig.CLIENT_ID,
        @Field("code") code: String = "code",
        @Field("code_verifier") codeVerifier: String = PKCEUtil.generateCodeVerifier(),
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("redirect_uri") redirectUri: String = BeyondIdentityConfig.REDIRECT_URI,
        @Field("state") state: String = "state"
    ): Response<TokenResponse>
}

object BeyondIdentityRetrofitBuilder {
    private fun getBeyondIdentityRetrofit() = Retrofit.Builder()
        .baseUrl("https://beyondidentity-cloud.byndid.com")
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { level = BODY })
                .build()
        )
        .build()

    val BEYOND_IDENTITY_API_SERVICE: BeyondIdentityApiService by lazy {
        getBeyondIdentityRetrofit().create(
            BeyondIdentityApiService::class.java
        )
    }
}
