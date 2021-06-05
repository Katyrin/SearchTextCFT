package com.katyrin.searchtext.myview

import android.view.View
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class TextInputLayoutHelperTextEnabledMatcher(private val enabled: Boolean) : TypeSafeMatcher<View>() {
    override fun matchesSafely(item: View?): Boolean {
        return item?.let {
            if (item is TextInputLayout) {
                item.isHelperTextEnabled
                item.isHelperTextEnabled == enabled
            } else false
        } ?: false
    }

    override fun describeTo(desc: Description) {
        desc.appendText("with HelperText state: ")
            .appendValue(enabled)
    }
}