package ru.rsreu.translate.api.translate

interface TranslateService {
    fun translate(source: String?, target: String, texts: List<String>): String
}