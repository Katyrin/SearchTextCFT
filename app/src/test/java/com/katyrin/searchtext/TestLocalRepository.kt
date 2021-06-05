package com.katyrin.searchtext

import com.katyrin.searchtext.repository.LocalRepository
import com.katyrin.searchtext.repository.LocalRepositoryImpl
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class TestLocalRepository(
    private val target: String,
    private val expected: Int,
    private val assetsText: String
) {

    companion object {

        @JvmStatic
        @Parameterized.Parameters
        fun data() = listOf(
            arrayOf(
                "rx",
                2,
                "RxKotlin is a lightweight library that adds convenient extension functions to RxJava"
            ),
            arrayOf(
                "kotlin",
                2,
                "You can use RxJava with Kotlin out-of-the-box, but Kotlin has language features " +
                        "(such as extension functions) that can streamline usage of RxJava even more."
            ),
            arrayOf(
                "co",
                4,
                "RxKotlin aims to conservatively collect these conveniences in one centralized " +
                        "library, and standardize conventions for using RxJava with Kotlin."
            )
        )
    }

    @Test
    fun `WHEN assertText contains incoming values EXPECT correct result`() {
        val localRepository: LocalRepository = LocalRepositoryImpl(assetsText)
        val list = localRepository.getMatchesStartEndPosition(target)
        assertTrue(list.size == expected)
    }

    @Test(expected = AssertionError::class)
    fun `WHEN assertText is not contains incoming values EXPECT AssertionError`() {
        val localRepository: LocalRepository = LocalRepositoryImpl(assetsText)
        val list = localRepository.getMatchesStartEndPosition(target)
        assertTrue(list.size == 1)
    }
}