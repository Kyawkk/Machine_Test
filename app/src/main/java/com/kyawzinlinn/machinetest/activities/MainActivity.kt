package com.kyawzinlinn.machinetest.activities

import android.app.UiModeManager.MODE_NIGHT_NO
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.kyawzinlinn.machinetest.adapter.PokeMonAdapter
import com.kyawzinlinn.machinetest.databinding.ActivityMainBinding
import com.kyawzinlinn.machinetest.models.PokeMonResult
import com.kyawzinlinn.machinetest.utils.POKEMON_INTENT_EXTRA
import com.kyawzinlinn.machinetest.utils.showErrorDialog
import com.kyawzinlinn.machinetest.viewmodel.Loading
import com.kyawzinlinn.machinetest.viewmodel.PokemonViewModel

class MainActivity : AppCompatActivity() {

     private lateinit var viewModel: PokemonViewModel
     private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PokemonViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)

        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        setContentView(binding.root)

        bindUI()

    }

    private fun bindUI(){
        loadAllPokeMons()
        searchPokeMonAction()
    }

    private fun loadAllPokeMons(){
        viewModel.apply {
            getAllPokeMons()
            status.observe(this@MainActivity){status ->
                when(status){
                    Loading.LOADING -> binding.loadingLayout.root.visibility = View.VISIBLE
                    Loading.DONE -> {
                        allPokeMons.observe(this@MainActivity){
                            binding.loadingLayout.root.visibility = View.GONE
                            setUpPokeMonRecyclerView(it.results)
                        }
                    }
                    Loading.ERROR -> {
                        error.observe(this@MainActivity){
                            showErrorDialog(
                                this@MainActivity,
                                "Error!",
                                it,
                                onRetry = {
                                    loadAllPokeMons()
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setUpPokeMonRecyclerView(pokeMons: List<PokeMonResult>){

        if (pokeMons.size == 0) binding.noItemLayout.root.visibility = View.VISIBLE
        else binding.noItemLayout.root.visibility = View.GONE


        binding.rvPokemon.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@MainActivity,2)
            val adapter = PokeMonAdapter(){ pokeMonResult, view ->
                val intent = Intent(this@MainActivity, PokeMonDetailActivity::class.java)

                view.transitionName = "itemTransition"
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity,
                    Pair(view,view.transitionName)
                )

                intent.putExtra(POKEMON_INTENT_EXTRA, pokeMonResult)
                startActivity(intent,options.toBundle())
            }
            setAdapter(adapter)
            adapter.submitList(pokeMons)
        }
    }

    private fun searchPokeMonAction(){
        binding.edPokemon.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(binding.edPokemon.text.toString().trim())
                true
            } else {
                false
            }
        }

        binding.edPokemon.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length == 0) loadAllPokeMons()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    private fun performSearch(query: String){
        viewModel.apply {
            searchPokeMon(query)
            searchStatus.observe(this@MainActivity){status ->
                when(status){
                    Loading.LOADING -> binding.loadingLayout.root.visibility = View.VISIBLE
                    Loading.DONE -> {
                        binding.loadingLayout.root.visibility = View.GONE
                        searchPokemonResponse.observe(this@MainActivity){
                            setUpPokeMonRecyclerView(listOf(it.species))
                        }
                    }
                    Loading.ERROR -> {
                        binding.loadingLayout.root.visibility = View.GONE
                        setUpPokeMonRecyclerView(listOf())
                    }
                }
            }
        }
    }
}