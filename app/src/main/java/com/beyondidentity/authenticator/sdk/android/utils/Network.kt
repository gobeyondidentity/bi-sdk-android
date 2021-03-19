package com.beyondidentity.authenticator.sdk.android.utils

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class BalanceResponse(
    @SerializedName("user_name")
    val userName: String,
    val balance: Int,
)

interface ApiService {
    @GET("balance")
    suspend fun getBalance(@Query("session") session: String): BalanceResponse
}

object RetrofitBuilder {

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ACME_CLOUD_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy { getRetrofit().create(ApiService::class.java) }
}
