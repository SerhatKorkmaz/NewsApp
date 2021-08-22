package com.example.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.newsapp.R
import com.example.newsapp.data.NewsData
import com.example.newsapp.databinding.ItemNewsapiBinding

class NewsAdapter(private val listener : OnItemClickListener) : PagingDataAdapter<NewsData, NewsAdapter.NewsViewHolder>(
    NEWS_COMPARATOR
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsapiBinding.inflate(LayoutInflater.from(parent.context), parent , false)

        return  NewsViewHolder(binding);
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = getItem(position)

        if(currentItem != null) holder.bind(currentItem)
    }

    inner class NewsViewHolder(private val binding: ItemNewsapiBinding) : RecyclerView.ViewHolder(binding.root){

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION){
                    val item = getItem(position)
                    if(item != null){
                        listener.onItemCLick(item)
                    }
                }
            }
        }
        fun bind(newsItem : NewsData){
            binding.apply {
                tvTitle.text = newsItem.title
                tvBody.text = newsItem.description
                Glide.with(itemView).load(newsItem.urlToImage)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(iwImage)

            }
        }
    }

    interface OnItemClickListener{
        fun onItemCLick(new : NewsData)
    }

    companion object{
         private val NEWS_COMPARATOR = object : DiffUtil.ItemCallback<NewsData>(){
             override fun areItemsTheSame(oldItem: NewsData, newItem: NewsData): Boolean =
                 oldItem.url == newItem.url

             override fun areContentsTheSame(oldItem: NewsData, newItem: NewsData): Boolean =
                 oldItem == newItem
         }
    }

}