package com.kyawzinlinn.machinetest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.flexbox.FlexboxLayoutManager
import com.kyawzinlinn.machinetest.databinding.PokemonSpriteItemBinding
import com.kyawzinlinn.machinetest.models.detail.Sprite

class SpritesAdapter: ListAdapter<String,SpritesAdapter.ViewHolder>(DiffCallBack) {

    class ViewHolder(private val binding: PokemonSpriteItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(img: String){
            binding.ivPokemonSprite.load(img){crossfade(true)}
        }
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(PokemonSpriteItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        /*val layoutParams = holder.itemView.layoutParams as FlexboxLayoutManager.LayoutParams
        layoutParams.flexGrow = 1f
        layoutParams.flexBasisPercent = 0.25f
        holder.itemView.layoutParams = layoutParams*/

        holder.bind(getItem(position))
    }
}