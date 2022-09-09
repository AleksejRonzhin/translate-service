package ru.rsreu.translate.api.translate.model

data class WordTranslation(
    val id: Long? = null,
    val word: String,
    val translatedWord: String
)
