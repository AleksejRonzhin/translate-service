package ru.rsreu.translate.api.translate.yandex.dto

data class YandexTranslateRequest(
    val sourceLanguageCode: String?, val targetLanguageCode: String, val texts: List<String>
)