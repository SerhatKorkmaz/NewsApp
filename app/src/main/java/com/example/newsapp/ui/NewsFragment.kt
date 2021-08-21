package com.example.newsapp.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.newsapp.R
import com.example.newsapp.data.NewsData
import com.example.newsapp.data.NewsViewModel
import com.example.newsapp.databinding.FragmentNewsBinding
import com.example.newsapp.databinding.LoadStateFooterBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : Fragment(R.layout.fragment_news), NewsAdapter.OnItemClickListener{

    private val viewModel by viewModels<NewsViewModel>()

    private var _binding : FragmentNewsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentNewsBinding.bind(view)

        val navBar: BottomNavigationView = requireActivity()!!.findViewById(com.example.newsapp.R.id.bottom_nav)
        navBar.isVisible = true

        val adapter = NewsAdapter(this)

        binding.apply {
            recyclerview.setHasFixedSize(true)
            recyclerview.adapter = adapter.withLoadStateHeaderAndFooter(
                header = NewsLoadStateAdapter {adapter.retry()},
                footer = NewsLoadStateAdapter {adapter.retry()}
            )

            bRetry.setOnClickListener{
                adapter.retry()
            }
        }

        viewModel.news.observe(viewLifecycleOwner){
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.addLoadStateListener { LoadState->
            binding.apply {
                pbNews.isVisible = LoadState.source.refresh is LoadState.Loading
                recyclerview.isVisible = LoadState.source.refresh is LoadState.NotLoading
                bRetry.isVisible = LoadState.source.refresh is LoadState.Error
                tvWarningConnection.isVisible = LoadState.source.refresh is LoadState.Error

                if(LoadState.source.refresh is LoadState.NotLoading && LoadState.append.endOfPaginationReached && adapter.itemCount < 1){
                    recyclerview.isVisible = false
                    tvWarningNodata.isVisible = true
                }
                else
                    tvWarningNodata.isVisible = false
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onItemCLick(new: NewsData) {
         val action = NewsFragmentDirections.actionNewsFragmentToDetailsFragment(new)
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_news, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null){
                    binding.recyclerview.scrollToPosition(0)
                    viewModel.searchNews(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}