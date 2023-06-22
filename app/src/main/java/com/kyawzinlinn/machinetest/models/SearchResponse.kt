package com.kyawzinlinn.machinetest.models

import com.kyawzinlinn.machinetest.models.detail.Sprite

data class SearchResponse(
    val species: PokeMonResult,
    val sprites: Sprite
)