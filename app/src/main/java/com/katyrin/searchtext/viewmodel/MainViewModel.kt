package com.katyrin.searchtext.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.katyrin.searchtext.App
import com.katyrin.searchtext.data.ResultItem
import com.katyrin.searchtext.utils.HALF_SECOND
import com.katyrin.searchtext.utils.toWords
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.BufferedReader
import java.io.InputStream
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val assetsText: String
): ViewModel() {

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
                .subscribe({ searchText ->
                    postValueSearchResults(searchText)
                }, {
                    _liveData.postValue(AppState.Error(it))
                })
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

    fun getAssetsText() {
        _liveData.value = AppState.Loading
        words = assetsText.toWords()
        _liveData.value = AppState.SuccessGetText(assetsText)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}