package ru.rsreu.translate.api.translate.yandex.dto

data class YandexTranslateResponse(
    val translations: List<Translation>
)

data class Translation(val text: String)