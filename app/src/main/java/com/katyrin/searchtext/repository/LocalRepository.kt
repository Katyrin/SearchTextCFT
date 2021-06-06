package com.katyrin.searchtext.repository

import io.reactivex.Single

interface LocalRepository {
    fun getMatchesStartEndPosition(word: String): Single<List<Pair<Int, Int>>>
    fun getAssetsText(): String
}