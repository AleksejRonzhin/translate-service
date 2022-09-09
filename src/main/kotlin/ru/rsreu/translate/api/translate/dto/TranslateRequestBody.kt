package ru.rsreu.translate.api.translate.dto

data class TranslateRequestBody(
    val source: String?,
    val target: String,
    val text: String
)