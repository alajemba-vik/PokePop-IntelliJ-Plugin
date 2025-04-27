package com.alaje.intellijplugins.pokepop.utils

object ValidationUtil {
    val String.isValidTimeFormat: Boolean get(){
        return matches(Regex("([01][0-9]|2[0-3]):[0-5][0-9]"))
    }
}