package ru.rsreu.translate.api.translate

import ru.rsreu.translate.api.translate.model.TranslateRequestInfo

interface TranslateService {
    fun translate(source: String?, target: String, text: String): String
    fun getAll(): List<TranslateRequestInfo>
}