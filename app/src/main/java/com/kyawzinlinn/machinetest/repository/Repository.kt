package com.kyawzinlinn.machinetest.repository

import com.kyawzinlinn.machinetest.models.AllPokemonsResponse
import com.kyawzinlinn.machinetest.models.SearchResponse
import com.kyawzinlinn.machinetest.models.detail.DetailResponse
import com.kyawzinlinn.machinetest.network.ApiService
import retrofit2.Response

class Repository(private val apiService: ApiService) {
    suspend fun getAllPokemons(): Response<AllPokemonsResponse> {
        return apiService.getAllPokemons("40")
    }

    suspend fun getPokeMonDetail(name: String): Response<DetailResponse>{
        return apiService.getPokeMonDetail(name)
    }

    suspend fun searchPokeMon(name: String): Response<SearchResponse>{
        return apiService.searchPokeMon(name)
    }

}