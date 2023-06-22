package com.kyawzinlinn.machinetest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.kyawzinlinn.machinetest.databinding.PokemonItemBinding
import com.kyawzinlinn.machinetest.models.PokeMonResult
import com.kyawzinlinn.machinetest.utils.IMG_FRONT
import com.kyawzinlinn.machinetest.utils.getImgIndex
import kotlin.reflect.KProperty0

class PokeMonAdapter(private val onItemClicked: (PokeMonResult, View) -> Unit): ListAdapter<PokeMonResult, PokeMonAdapter.ViewHolder>(DiffCallBack) {
    class ViewHolder(private val binding: PokemonItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(result: PokeMonResult, position: Int, onItemClicked: (PokeMonResult,View) -> Unit){
            binding.apply{
                ivPokemon.load("$IMG_FRONT${getImgIndex(result.url)}.png"){crossfade(true)}
                tvPokemonId.text = String.format("%03d",position)
                tvPokemonName.text = result.name
            }

            itemView.setOnClickListener { onItemClicked(result,itemView) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(PokemonItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position),position + 1,onItemClicked)
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<PokeMonResult>(){
        override fun areItemsTheSame(oldItem: PokeMonResult, newItem: PokeMonResult): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PokeMonResult, newItem: PokeMonResult): Boolean {
            return oldItem.url == newItem.url
        }

    }
}