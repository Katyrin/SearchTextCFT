package com.katyrin.searchtext.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.katyrin.searchtext.databinding.FragmentMainBinding
import com.katyrin.searchtext.viewmodel.AppState
import com.katyrin.searchtext.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createRecyclerView()
        setSearchEditText()
        viewModel.liveData.observe(viewLifecycleOwner) {
            renderData(it)
        }
        viewModel.getAllText()
    }

    private fun renderData(appState: AppState) {
        when(appState) {
            is AppState.SuccessGetText -> {
                binding.progressBar.isVisible = false
                val text = appState.text
                binding.allText.text = text
            }
            is AppState.Loading -> {
                binding.progressBar.isVisible = true
            }
            is AppState.Error -> {

            }
            is AppState.GetSearchResults -> {
                (binding.recyclerView.adapter as SearchAdapter).filterResult(appState.results)
            }
        }
    }

    private fun createRecyclerView() {
        binding.recyclerView.adapter = SearchAdapter()
    }

    private fun setSearchEditText() {
        binding.searchEditText.addTextChangedListener { text ->
            viewModel.filterResult(text)
        }
        binding.searchEditText.addTextChangedListener {}
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}