package com.katyrin.searchtext.utils

fun String.toWords() = trim()
    .replace(REGEX_REPLACE.toRegex(), DELIMITER)
    .split(REGEX_SPLIT.toRegex())
    .filter { it.isNotEmpty() }
    .toList()