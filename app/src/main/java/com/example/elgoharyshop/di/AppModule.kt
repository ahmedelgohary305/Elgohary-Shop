package com.example.elgoharyshop.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.example.elgoharyshop.auth.data.AuthRepoImpl
import com.example.elgoharyshop.auth.data.CustomerAccessTokenInterceptor
import com.example.elgoharyshop.auth.domain.AuthRepo
import com.example.elgoharyshop.shop.data.AppDataStore
import com.example.elgoharyshop.shop.data.ShopRepoImpl
import com.example.elgoharyshop.shop.data.local.WishListDatabase
import com.example.elgoharyshop.shop.domain.ShopRepo
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import okhttp3.OkHttpClient
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideWishListDatabase(@ApplicationContext context: Context): WishListDatabase {
        return Room.databaseBuilder(
            context,
            WishListDatabase::class.java,
            "wishlist_db"
        ).build()
    }

    @Provides
    fun provideCartDataStore(@ApplicationContext context: Context): AppDataStore {
        return AppDataStore(context)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        appDataStore: AppDataStore
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(CustomerAccessTokenInterceptor(appDataStore))
            .build()
    }

    @Provides
    @Singleton
    fun provideApolloClient(
        okHttpClient: OkHttpClient
    ): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl("https://elgohary-shop.myshopify.com/api/2023-07/graphql.json")
            .addHttpHeader("X-Shopify-Storefront-Access-Token", "bb5634b7e9a0c8e6d45a78a7771f7806")
            .okHttpClient(okHttpClient)
            .build()
    }


    @Provides
    @Singleton
    fun provideShopRepo(
        apolloClient: ApolloClient,
        appDataStore: AppDataStore,
        wishListDatabase: WishListDatabase
    ): ShopRepo {
        return ShopRepoImpl(apolloClient, appDataStore, wishListDatabase)
    }
}