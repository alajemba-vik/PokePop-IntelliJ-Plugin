package com.alaje.intellijplugins.pokepop.utils

object StringUtil {
    fun String.substringOrNull(startIndex: Int, endIndex: Int): String? {
        return try{
            substring(startIndex, endIndex)
        } catch (e: Exception){
            null
        }
    }
}