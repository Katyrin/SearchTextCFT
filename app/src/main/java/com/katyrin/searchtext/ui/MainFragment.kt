package com.katyrin.searchtext.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.katyrin.searchtext.databinding.FragmentMainBinding
import com.katyrin.searchtext.viewmodel.AppState
import com.katyrin.searchtext.viewmodel.MainViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

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
        initViews()
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

    private fun initViews() {
        binding.apply {
            searchEditText.addTextChangedListener { text ->
                filterResults(text.toString())
            }
            searchEditText.setOnClickListener {
                filterResults(searchEditText.text.toString())
            }
            allText.setOnClickListener {
                viewModel.clearResults()
            }
        }
    }

    private fun filterResults(text: String) {
        val textInput = Flowable.create<String>({ it.onNext(text) }, BackpressureStrategy.LATEST)
        viewModel.filterResults(textInput)
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}