package com.katyrin.searchtext.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.katyrin.searchtext.R
import com.katyrin.searchtext.databinding.FragmentMainBinding
import com.katyrin.searchtext.viewmodel.AppState
import com.katyrin.searchtext.viewmodel.MainViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import javax.inject.Inject

class MainFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: MainViewModel by viewModels(factoryProducer = { factory })
    private lateinit var binding: FragmentMainBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).appComponent.inject(this)
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
        viewModel.liveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.getAssetsText()
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
                binding.progressBar.isVisible = false
                showAlertDialog(appState.error.localizedMessage)
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

    private fun showAlertDialog(message: String?) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.error)
            .setMessage(message)
            .create()
            .show()
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}