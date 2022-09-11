package ru.rsreu.translate.api.translate.yandex.rest

import kotlinx.serialization.Serializable
import ru.rsreu.translate.api.translate.exception.TranslateServiceExceptionInfo

@Serializable
data class YandexTranslateServiceExceptionInfo(
    val code: Int, val message: String, val details: Collection<Detail>
) : TranslateServiceExceptionInfo()

@Serializable
data class Detail(
    val `@type`: String, val requestId: String
)