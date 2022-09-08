package ru.rsreu.translate.api.translate.dto

data class TranslateRequest(
    val source: String?,
    val target: String,
    val text: String
)