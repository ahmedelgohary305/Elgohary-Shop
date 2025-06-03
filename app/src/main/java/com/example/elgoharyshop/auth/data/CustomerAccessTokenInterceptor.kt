package com.example.elgoharyshop.auth.data

import com.example.elgoharyshop.shop.data.AppDataStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class CustomerAccessTokenInterceptor(
    private val appDataStore: AppDataStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        val token = runBlocking { appDataStore.getCustomerAccessToken() }
        if (!token.isNullOrBlank()) {
            requestBuilder.addHeader("X-Shopify-Customer-Access-Token", token)
        }
        return chain.proceed(requestBuilder.build())
    }
}
