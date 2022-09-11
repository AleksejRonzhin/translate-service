package ru.rsreu.translate.api.translate.model

import java.sql.Timestamp

data class TranslateRequest(
    var id: Long? = null,
    val ip: String,
    val date: Timestamp,
    val sourceLanguageCode: String?,
    val targetLanguageCode: String,
    val inputText: String,
    var outputText: String,
    var translations: Collection<WordTranslation>
)
