package com.katyrin.searchtext.viewmodel

sealed class AppState {
    data class SuccessGetText(val text: String) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
    data class SuccessTextState(val startEndList: List<Pair<Int,Int>>, val count: Int): AppState()
    object EmptyTextState: AppState()
}
