package com.example.newsapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentSourceBinding

class WebViewFragment : Fragment(R.layout.fragment_source) {

    private val args by navArgs<WebViewFragmentArgs>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSourceBinding.bind(view)
        val url = args.toString().substring(27,args.toString().length-1)
        binding.webview.loadUrl(url)
    }

}