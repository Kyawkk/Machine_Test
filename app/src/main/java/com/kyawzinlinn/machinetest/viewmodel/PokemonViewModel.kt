package com.kyawzinlinn.machinetest.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyawzinlinn.machinetest.models.AllPokemonsResponse
import com.kyawzinlinn.machinetest.models.SearchResponse
import com.kyawzinlinn.machinetest.models.detail.DetailResponse
import com.kyawzinlinn.machinetest.network.Api
import com.kyawzinlinn.machinetest.repository.Repository
import kotlinx.coroutines.launch

enum class Loading{
    LOADING,DONE,ERROR
}

class PokemonViewModel: ViewModel() {
    private val _status = MutableLiveData<Loading>()
    val status = _status

    private val _searchStatus = MutableLiveData<Loading>()
    val searchStatus = _searchStatus
    
    private val _error = MutableLiveData<String>()
    val error = _error

    private val _allPokeMons = MutableLiveData<AllPokemonsResponse>()
    val allPokeMons = _allPokeMons

    private val _searchPokeMonResponse = MutableLiveData<SearchResponse>()
    val searchPokemonResponse = _searchPokeMonResponse

    private val _pokeMonDetailResponse = MutableLiveData<DetailResponse>()
    val pokeMonDetailResponse = _pokeMonDetailResponse

    fun getAllPokeMons(){

        viewModelScope.launch {
            _status.value = Loading.LOADING
            try {
                val response = Repository(Api.retrofitService).getAllPokemons()

                if (response.isSuccessful){
                    _status.value = Loading.DONE
                    _allPokeMons.value = response.body()
                }

                else {
                    _status.value = Loading.ERROR
                    _error.value = response.errorBody().toString()
                }
            }catch (e: Exception){
                _status.value = Loading.ERROR
                _error.value = e.message
            }
        }

    }

    fun searchPokeMon(name: String){
        viewModelScope.launch {
            _searchStatus.value = Loading.LOADING
            try {
                val response = Repository(Api.retrofitService).searchPokeMon(name)

                if (response.isSuccessful){
                    _searchStatus.value = Loading.DONE
                    _searchPokeMonResponse.value = response.body()
                }

                else {
                    _searchStatus.value = Loading.ERROR
                    _error.value = response.errorBody().toString()
                }
            }catch (e: Exception){
                _searchStatus.value = Loading.ERROR
                _error.value = e.message
            }
        }
    }

    fun getPokemonDetail(name: String){
        viewModelScope.launch {
            _status.value = Loading.LOADING
            try {
                val response = Repository(Api.retrofitService).getPokeMonDetail(name)

                if (response.isSuccessful){
                    _status.value = Loading.DONE
                    _pokeMonDetailResponse.value = response.body()
                }

                else {
                    _status.value = Loading.ERROR
                    _error.value = response.errorBody().toString()
                }
            }catch (e: Exception){
                _status.value = Loading.ERROR
                _error.value = e.message
            }
        }
    }


}