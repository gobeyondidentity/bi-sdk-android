package com.beyondidentity.authenticator.sdk.android.utils

import com.beyondidentity.authenticator.sdk.android.BuildConfig
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class CreateUserRequest(
    @SerializedName("binding_token_delivery_method")
    val bindingTokenDeliveryMethod: String,
    @SerializedName("external_id")
    val externalId: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("user_name")
    val userName: String,
    @SerializedName("display_name")
    val displayName: String,
)

data class RecoverUserRequest(
    @SerializedName("binding_token_delivery_method")
    val bindingTokenDeliveryMethod: String,
    @SerializedName("external_id")
    val externalId: String,
)

data class UserResponse(
    val user: User,
    val error: String,
)
data class User(
    @SerializedName("internal_id")
    val internalId: String,
    @SerializedName("external_id")
    val externalId: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("user_name")
    val userName: String,
    @SerializedName("display_name")
    val displayName: String,
    @SerializedName("date_created")
    val dateCreated: String,
    @SerializedName("date_modified")
    val dateModified: String,
    @SerializedName("status")
    val status: String,
)

interface AcmeApiService {
    @POST("users")
    suspend fun createUser(@Body createUserRequest: CreateUserRequest): UserResponse

    @POST("recover-user")
    suspend fun recoverUser(@Body recoverUserRequest: RecoverUserRequest): UserResponse
}

object RetrofitBuilder {
    private fun getAcmeRetrofit() = Retrofit.Builder()
        .baseUrl(BuildConfig.BUILD_CONFIG_ACME_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { level = BODY })
                .build()
        )
        .build()

    val ACME_API_SERVICE: AcmeApiService by lazy { getAcmeRetrofit().create(AcmeApiService::class.java) }
}
