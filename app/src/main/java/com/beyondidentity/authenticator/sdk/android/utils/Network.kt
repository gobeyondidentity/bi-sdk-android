package com.beyondidentity.authenticator.sdk.android.utils

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class CredentialBindingLinkRequest(
    @SerializedName("username")
    val username: String,
    @SerializedName("authenticator_type")
    val authenticatorType: String = "native",
    @SerializedName("delivery_method")
    val deliveryMethod: String = "return",
    @SerializedName("email")
    val email: String? = null,
)

data class RecoverCredentialBindingLinkRequest(
    @SerializedName("username")
    val username: String,
    @SerializedName("authenticator_type")
    val authenticatorType: String = "native",
    @SerializedName("delivery_method")
    val deliveryMethod: String = "return",
)

data class CredentialBindingResponse(
    @SerializedName("credential_binding_job")
    val credentialBindingJob: CredentialBindingJob?,
    @SerializedName("credential_binding_link")
    val credentialBindingLink: String?,
)

data class CredentialBindingJob(
    @SerializedName("authenticator_config_id")
    val authenticatorConfigId: String,
    @SerializedName("create_time")
    val createTime: String,
    @SerializedName("delivery_method")
    val deliveryMethod: String,
    @SerializedName("expire_time")
    val expireTime: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("identity_id")
    val identityId: String,
    @SerializedName("post_binding_redirect_uri")
    val postBindingRedirectUri: String,
    @SerializedName("realm_id")
    val realmId: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("tenant_id")
    val tenantId: String,
    @SerializedName("update_time")
    val updateTime: String,
)

interface AcmeApiService {
    @POST("credential-binding-link")
    suspend fun credentialBindingLink(@Body credentialBindingLinkRequest: CredentialBindingLinkRequest): Response<CredentialBindingResponse>

    @POST("recover-credential-binding-link")
    suspend fun recoverCredentialBindingLink(@Body recoverCredentialBindingLinkRequest: RecoverCredentialBindingLinkRequest): Response<CredentialBindingResponse>
}

object RetrofitBuilder {
    private fun getAcmeRetrofit() = Retrofit.Builder()
        .baseUrl("https://acme-cloud.byndid.com")
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { level = BODY })
                .build(),
        )
        .build()

    val ACME_API_SERVICE: AcmeApiService by lazy { getAcmeRetrofit().create(AcmeApiService::class.java) }
}
