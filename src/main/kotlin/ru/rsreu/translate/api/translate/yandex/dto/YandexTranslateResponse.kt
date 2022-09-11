package ru.rsreu.translate.api.translate.yandex.dto

data class YandexTranslateResponse(
    val translations: Collection<Translation>
)

data class Translation(val text: String)