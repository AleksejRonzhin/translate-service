package ru.rsreu.translate.api.translate.model

import java.sql.Timestamp
import java.time.Instant

data class TranslateRequestInfo(
    val id: Long? = null,
    val date: Timestamp = Timestamp.from(Instant.now()),
    val sourceLanguageCode: String?,
    val targetLanguageCode: String,
    val inputText: String,
    val outputText: String,
    val ip: String
)