package ru.rsreu.translate.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Configuration
data class DatabaseConfiguration(
    @Value("\${database.queries.get_all}") val getAllRequestsQuery: String,
    @Value("\${database.queries.get_translations}") val getTranslationsByRequestIdQuery: String,
    @Value("\${database.queries.create}") val createRequestQuery: String,
    @Value("\${database.queries.create_translation}") val createTranslationQuery: String,
    val translateRequestTableColumns: TranslateRequestTableColumns,
    val wordTranslationsTableColumns: WordTranslationTableColumns
)

@Component
data class TranslateRequestTableColumns(
    @Value("\${database.tables.translate_requests.columns.id}") val id: String,
    @Value("\${database.tables.translate_requests.columns.date}") val date: String,
    @Value("\${database.tables.translate_requests.columns.source}") val source: String,
    @Value("\${database.tables.translate_requests.columns.target}") val target: String,
    @Value("\${database.tables.translate_requests.columns.input}") val input: String,
    @Value("\${database.tables.translate_requests.columns.output}") val output: String,
    @Value("\${database.tables.translate_requests.columns.ip}") val ip: String
)

@Component
data class WordTranslationTableColumns(
    @Value("\${database.tables.word_translations.columns.id}") val id: String,
    @Value("\${database.tables.word_translations.columns.request_id}") val requestId: String,
    @Value("\${database.tables.word_translations.columns.word}") val word: String,
    @Value("\${database.tables.word_translations.columns.translated_word}") val translatedWord: String
)