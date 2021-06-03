package com.katyrin.searchtext.viewmodel

import com.katyrin.searchtext.data.ResultItem

sealed class AppState {
    data class SuccessGetText(val text: String) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
    data class GetSearchResults(val results: List<ResultItem>) : AppState()
}
