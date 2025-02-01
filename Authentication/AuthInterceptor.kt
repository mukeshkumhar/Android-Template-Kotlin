package com.example.woodscraft.Authentication

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private  val sharedPreferences: SharedPreferences): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = sharedPreferences.getString("AccessToken", null)
        val request = if (accessToken != null) {
            chain.request().newBuilder()
                .header("Authorization", "$accessToken")
                .build()
        } else {
            chain.request()
        }
        return chain.proceed(request)
    }
}