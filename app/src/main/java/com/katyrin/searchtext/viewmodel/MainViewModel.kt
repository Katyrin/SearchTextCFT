package com.katyrin.searchtext.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.katyrin.searchtext.*
import com.katyrin.searchtext.data.ResultItem
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class MainViewModel : ViewModel() {

    private val _liveData: MutableLiveData<AppState> = MutableLiveData<AppState>()
    val liveData: LiveData<AppState> = _liveData

    private val disposable = CompositeDisposable()
    private var results: MutableList<ResultItem> = mutableListOf()
    private var words: List<String> = listOf()

    fun clearResults() {
        results.clear()
        _liveData.value = AppState.GetSearchResults(results)
    }

    fun filterResults(textInput: Flowable<String>) {
        disposable.add(
            textInput
                .debounce(HALF_SECOND, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe{ searchText ->
                    postValueSearchResults(searchText)
                }
        )
    }

    private fun postValueSearchResults(searchText: String) {
        results.clear()
        if (searchText.isNotBlank()) searchMatches(searchText)
        _liveData.postValue(AppState.GetSearchResults(results))
    }

    private fun searchMatches(searchText: String) {
        words.map { word ->
            val wordLowerCase = word.toLowerCase(Locale.ROOT)
            val searchTextLowerCase = searchText.toLowerCase(Locale.ROOT)
            if (wordLowerCase.contains(searchTextLowerCase))
                results.add(ResultItem(word))
        }
    }

    fun getAllText() {
        _liveData.value = AppState.Loading
        val bufferReader = App.appInstance.assets.open(FILE_NAME).bufferedReader()
        val allText = bufferReader.use { it.readText() }
        words = allText.toWords()
        _liveData.value = AppState.SuccessGetText(allText)
    }

    private fun String.toWords() = trim()
        .replace(REGEX_REPLACE.toRegex(), DELIMITER)
        .split(REGEX_SPLIT.toRegex())
        .filter { it.isNotEmpty() }
        .toList()

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}