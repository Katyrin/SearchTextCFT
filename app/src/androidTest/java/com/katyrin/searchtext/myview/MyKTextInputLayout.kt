package com.katyrin.searchtext.myview

import android.view.View
import android.widget.EditText
import androidx.test.espresso.DataInteraction
import com.agoda.kakao.common.builders.ViewBuilder
import com.agoda.kakao.common.views.KBaseView
import com.agoda.kakao.edit.KEditText
import org.hamcrest.Matcher

class MyKTextInputLayout : KBaseView<MyKTextInputLayout>, MyTextInputLayoutAssertions {
    val edit: KEditText

    constructor(function: ViewBuilder.() -> Unit) : super(function) {
        edit = KEditText {
            isDescendantOfA(function)
            isAssignableFrom(EditText::class.java)
        }
    }

    constructor(parent: Matcher<View>, function: ViewBuilder.() -> Unit) : super(parent, function) {
        edit = KEditText {
            isDescendantOfA(function)
            isAssignableFrom(EditText::class.java)
        }
    }

    constructor(parent: DataInteraction, function: ViewBuilder.() -> Unit) : super(parent, function) {
        edit = KEditText {
            isDescendantOfA(function)
            isAssignableFrom(EditText::class.java)
        }
    }
}