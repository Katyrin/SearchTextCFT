package com.katyrin.searchtext.utils

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.katyrin.searchtext.R

fun Context.showAlertDialog(message: String?) {
    MaterialAlertDialogBuilder(this)
        .setTitle(R.string.error)
        .setMessage(message)
        .create()
        .show()
}

fun SpannableString.highlightText(pair: Pair<Int, Int>, color: Int) {
    setSpan(
        BackgroundColorSpan(color),
        pair.first,
        pair.second,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
}