package com.katyrin.searchtext.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.katyrin.searchtext.utils.HALF_SECOND
import com.katyrin.searchtext.utils.ONE_SYMBOL
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val assetsText: String,
    private val uiScheduler: Scheduler
) : ViewModel() {

    private val _liveData: MutableLiveData<AppState> = MutableLiveData<AppState>()
    val liveData: LiveData<AppState> = _liveData
    private val disposable = CompositeDisposable()

    fun filterResults(textInput: Flowable<String>) {
        disposable.add(
            textInput.debounce(HALF_SECOND, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .flatMap { text -> getFlowableSource(text) }
                .observeOn(uiScheduler)
                .subscribe(
                    { startEndList -> setTextState(startEndList) },
                    { _liveData.value = AppState.Error(it) }
                )
        )
    }

    private fun setTextState(startEndList: List<Pair<Int, Int>>) {
        val count = startEndList.size
        val textLength = assetsText.length + ONE_SYMBOL
        if (count == textLength)
            _liveData.value = AppState.EmptyTextState
        else
            _liveData.value = AppState.SuccessTextState(startEndList, count)
    }

    private fun getFlowableSource(text: String): Flowable<List<Pair<Int, Int>>> =
        Flowable.create(
            { it.onNext(getMatchesStartEndPosition(text)) },
            BackpressureStrategy.LATEST
        )

    private fun getMatchesStartEndPosition(word: String): List<Pair<Int, Int>> {
        val startEndList: MutableList<Pair<Int, Int>> = mutableListOf()
        val pattern = word.toLowerCase(Locale.ROOT).toRegex()
        val matches = pattern.findAll(assetsText.toLowerCase(Locale.ROOT))
        matches.forEach { startEndList.add(Pair(it.range.first, it.range.last + ONE_SYMBOL)) }
        return startEndList
    }

    fun getAssetsText() {
        _liveData.value = AppState.SuccessGetText(assetsText)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}