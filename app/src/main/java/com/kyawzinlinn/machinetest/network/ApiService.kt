package com.kyawzinlinn.machinetest.network

import androidx.viewpager2.widget.ViewPager2.OffscreenPageLimit
import com.kyawzinlinn.machinetest.models.AllPokemonsResponse
import com.kyawzinlinn.machinetest.models.SearchResponse
import com.kyawzinlinn.machinetest.models.detail.DetailResponse
import com.kyawzinlinn.machinetest.utils.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

var mHttpLoggingInterceptor = HttpLoggingInterceptor()
    .setLevel(HttpLoggingInterceptor.Level.BODY)

var mOkHttpClient = OkHttpClient
    .Builder()
    .addInterceptor(mHttpLoggingInterceptor)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    //.addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(mOkHttpClient)
    .build()

interface ApiService {
    @Headers("Accept: application/json")
    @GET("pokemon")
    suspend fun getAllPokemons(
        @Query("limit") limit: String
    ): Response<AllPokemonsResponse>

    @Headers("Accept: application/json")
    @GET("pokemon/{id}")
    suspend fun getPokeMonDetail(
        @Path("id") name: String
    ): Response<DetailResponse>

    @Headers("Accept: application/json")
    @GET("pokemon/{query}")
    suspend fun searchPokeMon(
        @Path("query") query: String
    ): Response<SearchResponse>
}

object Api{
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}