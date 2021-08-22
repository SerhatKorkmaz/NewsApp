package com.example.newsapp.ui

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.newsapp.databinding.FragmentNewsDetailsBinding
import android.util.Log
import com.example.newsapp.data.NewsData

import android.graphics.drawable.StateListDrawable
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.example.newsapp.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class DetailsFragment : Fragment(R.layout.fragment_news_details) {

    private val args by navArgs<DetailsFragmentArgs>()
    private var myNewsList = arrayListOf<NewsData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        requireContext().loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_details,menu)
        //menu.getItem(0).isChecked = myNewsList.get
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentNewsDetailsBinding.bind(view)

        binding.apply {
            val new = args.new

            Glide.with(this@DetailsFragment)
                .load(new.urlToImage)
                .error(R.drawable.ic_error)
                .listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        tvAuthor.isVisible = true
                        tvDate.isVisible = true
                        tvNewTitle.isVisible = true
                        tvBody.isVisible = true
                        return false
                    }
                })
                .into(iwNew)

            tvAuthor.text = new.author
            tvDate.text = new.publishedAt.substring(0,10)
            tvNewTitle.text = new.title
            tvBody.text = new.content

            bSource.setOnClickListener {
                val action = DetailsFragmentDirections.actionDetailsFragmentToWebViewFragment2(new.url)
                findNavController().navigate(action)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorite -> {
                val new = args.new
                myNewsList.add(new)
                for(item in myNewsList) Log.d("AAAAAAAAAAAAAA",item.url)
                requireContext().saveData(myNewsList)
                item.isChecked = !item.isChecked
                val stateListDrawable =
                    ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_selector, null) as StateListDrawable
                val state =
                    intArrayOf(if (item.isChecked) android.R.attr.state_checked else android.R.attr.state_empty)
                stateListDrawable.state = state
                item.icon = stateListDrawable.current

                true
            }
            R.id.action_share -> {
                val new = args.new
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, new.url)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun Context.loadData() {
        val sharedPreferences =  getSharedPreferences("sharedprefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("favorite_list", null)
        val type = object : TypeToken<ArrayList<NewsData?>?>() {}.type
        type?.let {type->
            (gson.fromJson<Any>(json, type) as? ArrayList<NewsData>)?.let {
                myNewsList =  it
            }
        }
    }

    fun Context.saveData(myNewsList:ArrayList<NewsData>) {
        val sharedPreferences = getSharedPreferences("sharedprefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(myNewsList)
        editor.putString("favorite_list", json)
        editor.apply()
        Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show()
    }
}