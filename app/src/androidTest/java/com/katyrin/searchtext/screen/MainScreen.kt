package com.katyrin.searchtext.screen

import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.screen.Screen
import com.katyrin.searchtext.R
import com.katyrin.searchtext.myview.MyKTextInputLayout

object MainScreen : Screen<MainScreen>() {
    val searchEditText = KEditText { withId(R.id.search_edit_text) }
    val textInputLayout: MyKTextInputLayout = MyKTextInputLayout { withId(R.id.text_input_layout) }
}