package com.kyawzinlinn.machinetest.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.kyawzinlinn.machinetest.R
import com.kyawzinlinn.machinetest.adapter.SpritesAdapter
import com.kyawzinlinn.machinetest.databinding.ActivityPokeMonDetailBinding
import com.kyawzinlinn.machinetest.models.PokeMonResult
import com.kyawzinlinn.machinetest.models.detail.Sprite
import com.kyawzinlinn.machinetest.utils.IMG_FRONT
import com.kyawzinlinn.machinetest.utils.POKEMON_INTENT_EXTRA
import com.kyawzinlinn.machinetest.utils.getImgIndex
import com.kyawzinlinn.machinetest.viewmodel.Loading
import com.kyawzinlinn.machinetest.viewmodel.PokemonViewModel

class PokeMonDetailActivity : AppCompatActivity() {
    private lateinit var viewModel: PokemonViewModel
    private lateinit var binding: ActivityPokeMonDetailBinding
    private lateinit var pokeMon: PokeMonResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PokemonViewModel::class.java)
        binding = ActivityPokeMonDetailBinding.inflate(layoutInflater)

        window.statusBarColor = resources.getColor(R.color.primary_color_5)

        setContentView(binding.root)

        pokeMon = intent!!.extras?.getSerializable(POKEMON_INTENT_EXTRA) as PokeMonResult

        bindUI()
        loadPokeMonDetail()
        setUpClickListeners()

    }

    private fun bindUI(){
        binding.apply {
            tvPokemonDetailTitle.text = pokeMon?.name
            tvPokemonDetailId.text = "#${String.format("%03d", getImgIndex(pokeMon.url).toInt())}"
            ivPokemonDetail.load("$IMG_FRONT${getImgIndex(pokeMon.url)}.png"){crossfade(true)}
        }
    }

    private fun loadPokeMonDetail(){
        viewModel.apply {
            getPokemonDetail(pokeMon.name)
            status.observe(this@PokeMonDetailActivity){status ->
                when (status){
                    Loading.LOADING -> binding.detailLoadingLayout.root.visibility = View.VISIBLE
                    Loading.DONE -> {
                        binding.detailLoadingLayout.root.visibility = View.GONE

                        pokeMonDetailResponse.observe(this@PokeMonDetailActivity){
                            binding.apply {
                                tvAbilityOne.text = it.abilities.get(0).ability.name
                                tvAbilityTwo.text = it.abilities.get(1).ability.name
                            }

                            Log.d("TAG", "loadPokeMonDetail: ${it.sprites}")

                            setUpSpriteRecyclerview(it.sprites)
                        }
                    }
                    Loading.ERROR -> {

                    }
                }
            }
        }
    }

    private fun setUpSpriteRecyclerview(sprite: Sprite) {
        binding.rvPokemonDetailSprites.apply {
            setHasFixedSize(true)
            layoutManager = FlexboxLayoutManager(this@PokeMonDetailActivity,FlexDirection.ROW).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.SPACE_EVENLY

            }
            val adapter = SpritesAdapter()
            this.adapter = adapter
            val data = listOf(
                sprite.back_default,
                sprite.back_female,
                sprite.back_shiny,
                sprite.back_shiny_female,
                sprite.front_default,
                sprite.front_female,
                sprite.front_shiny,
                sprite.front_shiny_female
            ).filter { it != null }
            adapter.submitList(data)
        }
    }

    private fun setUpClickListeners(){
        binding.apply {
            ivPokemonDetailBack.setOnClickListener { onBackPressed() }
        }
    }
}