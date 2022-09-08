package ru.rsreu.translate.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
data class DatabaseConfiguration(
    @Value("\${database.translate_requests.queries.get_all}") val getAllRequestsQuery: String,
    @Value("\${database.translate_requests.queries.get_translations}") val getTranslationsByRequestIdQuery: String,
    @Value("\${database.translate_requests.queries.create}") val createRequestQuery: String,
    @Value("\${database.translate_requests.queries.create_translation}") val createTranslationQuery: String,
)