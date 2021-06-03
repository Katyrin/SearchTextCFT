package com.katyrin.searchtext.viewmodel

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.katyrin.searchtext.*
import com.katyrin.searchtext.data.ResultItem
import java.util.*

class MainViewModel : ViewModel() {

    private val _liveData: MutableLiveData<AppState> = MutableLiveData<AppState>()
    val liveData: LiveData<AppState> = _liveData

    private var results: MutableList<ResultItem> = mutableListOf()
    private var words: List<String> = listOf()

    fun filterResult(searchText: Editable?) {
        Thread {
            results.clear()
            if (!searchText.isNullOrEmpty()) {
                words.map { word ->
                    val wordLowerCase = word.toLowerCase(Locale.ROOT)
                    val searchTextLowerCase = searchText.toString().toLowerCase(Locale.ROOT)
                    if (wordLowerCase.contains(searchTextLowerCase))
                        results.add(ResultItem(word))
                }
            }
            _liveData.postValue(AppState.GetSearchResults(results))
        }.start()
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
}