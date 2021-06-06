package com.katyrin.searchtext.repository

import com.katyrin.searchtext.utils.ONE_SYMBOL
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val assetsText: String
) : LocalRepository {

    override fun getMatchesStartEndPosition(word: String): Single<List<Pair<Int, Int>>> =
        Single.fromCallable { getStartEndList(word) }
            .subscribeOn(Schedulers.computation())

    private fun getStartEndList(word: String): List<Pair<Int, Int>> {
        val startEndList: MutableList<Pair<Int, Int>> = mutableListOf()
        val pattern = word.toLowerCase(Locale.ROOT).toRegex()
        val matches = pattern.findAll(assetsText.toLowerCase(Locale.ROOT))
        matches.forEach { startEndList.add(Pair(it.range.first, it.range.last + ONE_SYMBOL)) }
        return startEndList
    }

    override fun getAssetsText(): String {
        return assetsText
    }
}