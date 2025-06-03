package com.example.elgoharyshop.auth.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.example.elgoharyshop.CustomerAccessTokenCreateMutation
import com.example.elgoharyshop.CustomerAccessTokenDeleteMutation
import com.example.elgoharyshop.CustomerCreateMutation
import com.example.elgoharyshop.GetCustomerQuery
import com.example.elgoharyshop.GetCustomerQuery.Customer
import com.example.elgoharyshop.shop.data.AppDataStore
import com.example.elgoharyshop.shop.domain.ApiResult
import com.example.elgoharyshop.type.CustomerAccessTokenCreateInput
import com.example.elgoharyshop.type.CustomerCreateInput
import com.example.elgoharyshop.auth.domain.AuthRepo
import com.example.elgoharyshop.utils.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthRepoImpl @Inject constructor(
    private val apolloClient: ApolloClient,
    private val appDataStore: AppDataStore,
) : AuthRepo {
    override suspend fun signUpCustomer(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): ApiResult<Unit> = safeApiCall {
        val response = apolloClient.mutation(
            CustomerCreateMutation(
                CustomerCreateInput(
                    email = email, password = password,
                    firstName = Optional.Present(firstName),
                    lastName = Optional.Present(lastName)
                )
            )
        ).execute()

        val errors = response.data?.customerCreate?.userErrors
        if (!errors.isNullOrEmpty()) {
            throw Exception(errors.first().message)
        }
    }

    override suspend fun loginCustomer(email: String, password: String): ApiResult<String> = safeApiCall {
        val response = apolloClient.mutation(
            CustomerAccessTokenCreateMutation(
                CustomerAccessTokenCreateInput(email = email, password = password)
            )
        ).execute()

        val token = response.data?.customerAccessTokenCreate?.customerAccessToken?.accessToken
            ?: throw Exception("Login failed: ${response.data?.customerAccessTokenCreate?.userErrors?.firstOrNull()?.message}")

        appDataStore.saveCustomerAccessToken(token)
        token
    }

    override suspend fun logoutCustomer(token: String): ApiResult<Unit> = safeApiCall {
        apolloClient.mutation(
            CustomerAccessTokenDeleteMutation(token)
        ).execute()
        appDataStore.clearCustomerAccessToken()
    }

    override suspend fun getCustomerAccessToken(): String? {
        return appDataStore.getCustomerAccessToken()
    }

    override suspend fun getCustomer(): Customer? {
        val token = appDataStore.getCustomerAccessToken()
        if (token.isNullOrEmpty()) return null

        val response = apolloClient.query(
            GetCustomerQuery(customerAccessToken = token)
        ).execute()

        return response.data?.customer
    }


}