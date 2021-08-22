package com.example.newsapp.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapters.FavoritesAdapter
import com.example.newsapp.data.NewsData
import com.example.newsapp.databinding.FragmentFavoritesBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoritesFragment : Fragment(R.layout.fragment_favorites),FavoritesAdapter.OnItemClickListener {

    private var myNewsList = arrayListOf<NewsData>()
    private lateinit var adapter: FavoritesAdapter
    private var _binding : FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoritesBinding.bind(view)
        adapter = FavoritesAdapter(myNewsList,this)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().loadData()
    }

    override fun onItemClick(position: Int) {
        val new = myNewsList[position]
        val action = FavoritesFragmentDirections.actionFavoritesFragmentToDetailsFragment2(new)
        findNavController().navigate(action)
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
}