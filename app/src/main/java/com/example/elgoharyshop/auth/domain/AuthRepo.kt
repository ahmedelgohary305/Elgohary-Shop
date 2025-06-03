package com.example.elgoharyshop.auth.domain

import com.example.elgoharyshop.GetCustomerQuery.Customer
import com.example.elgoharyshop.shop.domain.ApiResult

interface AuthRepo {
    suspend fun signUpCustomer(email: String, password: String,firstName:String,lastName:String): ApiResult<Unit>
    suspend fun loginCustomer(email: String, password: String): ApiResult<String>
    suspend fun logoutCustomer(token: String): ApiResult<Unit>
    suspend fun getCustomerAccessToken(): String?
    suspend fun getCustomer(): Customer?

}