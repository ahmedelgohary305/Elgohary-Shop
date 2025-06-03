package com.example.elgoharyshop.shop.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "cart_prefs")

class AppDataStore(private val context: Context) {
    companion object {
        private val CART_ID_KEY = stringPreferencesKey("cart_id")
        private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        private val CUSTOMER_TOKEN_KEY = stringPreferencesKey("customer_token")
    }

    suspend fun saveCartId(id: String) {
        context.dataStore.edit { prefs ->
            prefs[CART_ID_KEY] = id
        }
    }

    suspend fun getCartId(): String? {
        return context.dataStore.data.map { it[CART_ID_KEY] }.first()
    }

    suspend fun clearCartId() {
        context.dataStore.edit { prefs ->
            prefs.remove(CART_ID_KEY)
        }
    }

    // --- Theme logic ---
    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[DARK_MODE_KEY] = enabled
        }
    }

    suspend fun getInitialDarkMode(): Boolean {
        return context.dataStore.data.map { it[DARK_MODE_KEY] ?: false }.first()
    }

    suspend fun saveCustomerAccessToken(token: String) {
        context.dataStore.edit { prefs -> prefs[CUSTOMER_TOKEN_KEY] = token }
    }

    suspend fun getCustomerAccessToken(): String? {
        return context.dataStore.data.map { it[CUSTOMER_TOKEN_KEY] }.firstOrNull()
    }

    suspend fun clearCustomerAccessToken() {
        context.dataStore.edit { prefs -> prefs.remove(CUSTOMER_TOKEN_KEY) }
    }

}

