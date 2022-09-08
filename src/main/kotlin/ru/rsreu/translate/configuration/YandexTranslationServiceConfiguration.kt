package ru.rsreu.translate.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders

@Configuration
data class YandexTranslationServiceConfiguration(
    @Value("\${yandex_translation_service.url}") val url: String,
    @Value("\${yandex_translation_service.bearer_token}") val bearerToken: String,
    @Value("\${yandex_translation_service.api.translate}") val translateUrlPart: String,
    @Value("\${yandex_translation_service.api.translate_error}") val translateErrorText: String,
    @Value("\${yandex_translation_service.api.detect}") val detect: String,
) {
    @Bean
    fun headers() = HttpHeaders().apply { setBearerAuth(bearerToken) }
}