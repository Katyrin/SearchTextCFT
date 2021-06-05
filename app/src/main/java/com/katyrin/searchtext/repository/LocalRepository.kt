package com.katyrin.searchtext.repository

interface LocalRepository {
    fun getMatchesStartEndPosition(word: String): List<Pair<Int, Int>>
    fun getAssetsText(): String
}