package com.example.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.newsapp.R
import com.example.newsapp.data.NewsData
import com.example.newsapp.databinding.FragmentFavoritesBinding
import com.example.newsapp.databinding.ItemNewsapiBinding

class FavoritesAdapter(val list: ArrayList<NewsData>,private val listener : OnItemClickListener) : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    inner class FavoritesViewHolder(val binding: ItemNewsapiBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener{
        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }

        init{
            itemView.setOnClickListener(this)
        }

    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val binding = ItemNewsapiBinding.inflate(LayoutInflater.from(parent.context), parent , false)

        return  FavoritesViewHolder(binding);
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                binding.apply {
                    tvTitle.text = list[position].title
                    tvBody.text = list[position].description
                    Glide.with(itemView).load(list[position].urlToImage)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_error)
                        .into(iwImage)

                }

            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}