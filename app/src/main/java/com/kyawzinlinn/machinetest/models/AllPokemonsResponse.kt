package com.kyawzinlinn.machinetest.models

data class AllPokemonsResponse(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<PokeMonResult>
)