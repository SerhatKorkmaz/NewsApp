package com.example.newsapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.newsapp.R
import com.example.newsapp.data.NewsViewModel
import com.example.newsapp.databinding.FragmentNewsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : Fragment(R.layout.fragment_news){

    private val viewModel by viewModels<NewsViewModel>()

    private var binding : FragmentNewsBinding? = null
    private val _binding get() = binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentNewsBinding.bind(view)

        val adapter = NewsAdapter()

        _binding.apply {
            recyclerview.setHasFixedSize(true)
            recyclerview.adapter = adapter
        }

        viewModel.news.observe(viewLifecycleOwner){
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}