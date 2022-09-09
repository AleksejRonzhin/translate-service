package ru.rsreu.translate.api.translate.service

interface TranslateService {
    fun translate(source: String?, target: String, texts: List<String>): String
}