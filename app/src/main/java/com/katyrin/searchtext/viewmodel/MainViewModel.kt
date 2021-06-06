package com.katyrin.searchtext.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.katyrin.searchtext.repository.LocalRepository
import com.katyrin.searchtext.utils.HALF_SECOND
import com.katyrin.searchtext.utils.ONE_SYMBOL
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val uiScheduler: Scheduler,
    private val localRepository: LocalRepository
) : ViewModel() {

    private val _liveData: MutableLiveData<AppState> = MutableLiveData<AppState>()
    val liveData: LiveData<AppState> = _liveData
    private val disposable = CompositeDisposable()

    fun subscribeFilterResults(textInput: Flowable<String>) {
        disposable.add(
            textInput
                .debounce(HALF_SECOND, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .switchMapSingle(localRepository::getMatchesStartEndPosition)
                .observeOn(uiScheduler)
                .subscribe(
                    { startEndList -> setTextState(startEndList) },
                    { _liveData.value = AppState.Error(it) }
                )
        )
    }

    private fun setTextState(startEndList: List<Pair<Int, Int>>) {
        val count = startEndList.size
        val textLength = localRepository.getAssetsText().length + ONE_SYMBOL
        if (count == textLength)
            _liveData.value = AppState.EmptyTextState
        else
            _liveData.value = AppState.SuccessTextState(startEndList, count)
    }

    fun getAssetsText() {
        _liveData.value = AppState.SuccessGetText(localRepository.getAssetsText())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}