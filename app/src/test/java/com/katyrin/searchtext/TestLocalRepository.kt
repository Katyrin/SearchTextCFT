package com.katyrin.searchtext

import com.katyrin.searchtext.repository.LocalRepository
import com.katyrin.searchtext.repository.LocalRepositoryImpl
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class TestLocalRepository(
    private val target: String,
    private val expected: List<Pair<Int, Int>>,
    private val assetsText: String
) {

    @Rule
    @JvmField
    val testRule = RxRule()

    companion object {

        @JvmStatic
        @Parameterized.Parameters
        fun data() = listOf(
            arrayOf(
                "rx",
                listOf(0 to 2, 78 to 80),
                "RxKotlin is a lightweight library that adds convenient extension functions to RxJava"
            ),
            arrayOf(
                "kotlin",
                listOf(24 to 30, 51 to 57),
                "You can use RxJava with Kotlin out-of-the-box, but Kotlin has language features " +
                        "(such as extension functions) that can streamline usage of RxJava even more."
            ),
            arrayOf(
                "co",
                listOf(17 to 19, 32 to 34, 46 to 48, 103 to 105),
                "RxKotlin aims to conservatively collect these conveniences in one centralized " +
                        "library, and standardize conventions for using RxJava with Kotlin."
            )
        )
    }

    @Test
    fun `WHEN assertText contains incoming values EXPECT correct result`() {
        val localRepository: LocalRepository = LocalRepositoryImpl(assetsText)
        val list = localRepository.getMatchesStartEndPosition(target)

        list
            .test()
            .assertValue(expected)
    }

    @Test(expected = AssertionError::class)
    fun `WHEN assertText is not contains incoming values EXPECT AssertionError`() {
        val localRepository: LocalRepository = LocalRepositoryImpl(assetsText)
        val list = localRepository.getMatchesStartEndPosition(target)

        list
            .test()
            .assertError(AssertionError::class.java)
    }
}