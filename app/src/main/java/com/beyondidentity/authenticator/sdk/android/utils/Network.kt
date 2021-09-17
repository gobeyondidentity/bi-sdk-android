package com.beyondidentity.authenticator.sdk.android.utils

import com.beyondidentity.authenticator.sdk.android.BuildConfig
import com.google.gson.annotations.SerializedName
import java.io.IOException
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class BalanceResponse(
    @SerializedName("user_name")
    val userName: String,
    val balance: Int,
)

data class TokenResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("id_token")
    val idToken: String,
)

interface AcmeApiService {
    @GET("balance")
    suspend fun getBalance(@Query("session") session: String): BalanceResponse
}

interface BiApiService {
    @FormUrlEncoded
    @POST("v2/token")
    suspend fun getToken(
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("grant_type") grantType: String,
        @Field("code_verifier") code_verifier: String?,
    ): TokenResponse
}

object RetrofitBuilder {

    private fun getOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = BODY

        return OkHttpClient.Builder()
            .addInterceptor(
                BasicAuthInterceptor(
                    user = BuildConfig.BUILD_CONFIG_BI_DEMO_CONFIDENTIAL_CLIENT_ID,
                    // !!! WARNING !!!
                    // Never expose the client secret in your public clients (mobile app, front ent)
                    // This is just for demo purposes.
                    password = BuildConfig.BUILD_CONFIG_BI_DEMO_CONFIDENTIAL_CLIENT_SECRET,
                )
            )
            .addInterceptor(interceptor)
            .build()
    }

    private fun getAcmeRetrofit() = Retrofit.Builder()
        .baseUrl(ACME_CLOUD_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun getBiApiRetrofit() = Retrofit.Builder()
        .baseUrl(BuildConfig.BUILD_CONFIG_AUTH_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(getOkHttpClient())
        .build()

    val ACME_API_SERVICE: AcmeApiService by lazy { getAcmeRetrofit().create(AcmeApiService::class.java) }
    val BI_API_SERVICE: BiApiService by lazy { getBiApiRetrofit().create(BiApiService::class.java) }
}


class BasicAuthInterceptor(user: String, password: String) :
    Interceptor {
    private val credentials: String = Credentials.basic(user, password)

    @Throws(IOException::class)
    override fun intercept(chain: Chain): Response {
        val request: Request = chain.request()
        val authenticatedRequest: Request = request.newBuilder()
            .header("Authorization", credentials).build()
        return chain.proceed(authenticatedRequest)
    }

}