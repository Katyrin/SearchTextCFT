package com.katyrin.searchtext.ui

import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.text.clearSpans
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.katyrin.searchtext.R
import com.katyrin.searchtext.databinding.FragmentMainBinding
import com.katyrin.searchtext.utils.highlightText
import com.katyrin.searchtext.utils.showAlertDialog
import com.katyrin.searchtext.viewmodel.AppState
import com.katyrin.searchtext.viewmodel.MainViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class MainFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: MainViewModel by viewModels(factoryProducer = { factory })
    private lateinit var binding: FragmentMainBinding
    private lateinit var spannable: SpannableString
    private val spanColor by lazy { requireContext().resources.getColor(R.color.purple_200) }
    private val inputManager: InputMethodManager by lazy {
        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private val _textInput = BehaviorSubject.create<String>()
    private val textInput = _textInput.toFlowable(BackpressureStrategy.LATEST)

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
        initViews()
        viewModel.liveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.getAssetsText()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.SuccessGetText -> {
                setText(appState.text)
            }
            is AppState.Loading -> {
                binding.progressBar.isVisible = true
            }
            is AppState.Error -> {
                binding.progressBar.isVisible = false
                requireContext().showAlertDialog(appState.error.localizedMessage)
            }
            is AppState.SuccessTextState -> {
                setTextState(appState.startEndList, appState.count)
            }
            is AppState.EmptyTextState -> {
                setEmptyTextState()
            }
        }
    }

    private fun setText(text: String) {
        binding.progressBar.isVisible = false
        spannable = SpannableString(text)
        binding.allText.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    private fun setTextState(startEndList: List<Pair<Int, Int>>, count: Int) {
        val helperText = requireContext().getString(R.string.text_matches) + " $count"
        spannable.clearSpans()
        startEndList.map { pair -> spannable.highlightText(pair, spanColor) }
        binding.allText.setText(spannable, TextView.BufferType.SPANNABLE)
        binding.textInputLayout.helperText = helperText
    }

    private fun setEmptyTextState() {
        spannable.clearSpans()
        binding.textInputLayout.helperText = ""
        binding.allText.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    private fun initViews() {
        binding.searchEditText.apply {
            addTextChangedListener { text ->
                filterResults(text.toString())
            }
            setOnEditorActionListener { _, actionId, _ ->
                hideKeyboard(actionId)
                return@setOnEditorActionListener true
            }
        }
    }

    private fun hideKeyboard(actionId: Int) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            inputManager.hideSoftInputFromWindow(
                requireActivity().currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
            binding.textInputLayout.clearFocus()
        }
    }

    private fun filterResults(text: String) {
        _textInput.onNext(text)
        viewModel.filterResults(textInput)
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}