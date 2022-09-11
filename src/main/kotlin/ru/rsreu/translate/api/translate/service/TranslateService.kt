package ru.rsreu.translate.api.translate.service

interface TranslateService {
    val key: String
    fun translate(source: String?, target: String, texts: Collection<String>): Collection<String>
}