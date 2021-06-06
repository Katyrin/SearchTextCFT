package com.katyrin.searchtext


import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.katyrin.searchtext.screen.MainScreen
import com.katyrin.searchtext.ui.MainActivity
import com.katyrin.searchtext.utils.TEST_600_MILLISECONDS
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CountMatchesTest : KTestCase() {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)


    @Test
    fun checkMatches() {
        run {
            step("type 'flat'") {
                MainScreen {
                    searchEditText {
                        isDisplayed()
                        typeText("flat")
                        Thread.sleep(TEST_600_MILLISECONDS)
                    }
                    textInputLayout {
                        isDisplayed()
                        isHelperTextEnabled()
                        hasHelperText("Совпадений в тексте: 4")
                    }
                }
            }
            step("clear") {
                MainScreen {
                    searchEditText {
                        isDisplayed()
                        clearText()
                        Thread.sleep(TEST_600_MILLISECONDS)
                    }
                    textInputLayout {
                        isDisplayed()
                        isHelperTextDisabled()
                        hasHelperText(null)
                    }
                }
            }
            step("type 'map'") {
                MainScreen {
                    searchEditText {
                        isDisplayed()
                        typeText("map")
                        Thread.sleep(TEST_600_MILLISECONDS)
                    }
                    textInputLayout {
                        isDisplayed()
                        isHelperTextEnabled()
                        hasHelperText("Совпадений в тексте: 10")
                    }
                }
            }
            step("clear") {
                MainScreen {
                    searchEditText {
                        isDisplayed()
                        clearText()
                        Thread.sleep(TEST_600_MILLISECONDS)
                    }
                    textInputLayout {
                        isDisplayed()
                        isHelperTextDisabled()
                        hasHelperText(null)
                    }
                }
            }
            step("type 'a'") {
                MainScreen {
                    searchEditText {
                        isDisplayed()
                        typeText("a")
                        Thread.sleep(TEST_600_MILLISECONDS)
                    }
                    textInputLayout {
                        isDisplayed()
                        isHelperTextEnabled()
                        hasHelperText("Совпадений в тексте: 29")
                    }
                }
            }
        }
    }
}