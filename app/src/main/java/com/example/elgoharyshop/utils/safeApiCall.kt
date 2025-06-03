package com.example.elgoharyshop.utils

import com.apollographql.apollo3.exception.ApolloException
import com.example.elgoharyshop.shop.domain.ApiResult

suspend fun <T> safeApiCall(apiCall: suspend () -> T): ApiResult<T> {
    return try {
        ApiResult.Success(apiCall())
    } catch (e: ApolloException) {
        ApiResult.Error("Network error: ${e.message ?: "Unknown network error"}")
    } catch (e: Exception) {
        ApiResult.Error(e.message ?: "An unknown error occurred")
    }
}