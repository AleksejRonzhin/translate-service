package ru.rsreu.translate.api.translate.yandex.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.client.RestTemplate
import ru.rsreu.translate.api.translate.yandex.YandexTranslateServiceHandler

@Configuration
data class YandexTranslateServiceConfiguration(
    @Value("\${yandex_translation_service.url}") val url: String,
    @Value("\${yandex_translation_service.bearer_token}") val bearerToken: String,
    @Value("\${yandex_translation_service.api.translate}") val translateUrlPart: String,
    @Value("\${yandex_translation_service.api.translate_error}") val translateErrorText: String,
    @Value("\${yandex_translation_service.api.detect}") val detect: String,
    private val restTemplateBuilder: RestTemplateBuilder,
    private val restTemplateHandler: YandexTranslateServiceHandler
) {
    fun headers() = HttpHeaders().apply { setBearerAuth(bearerToken) }

    fun restTemplate(): RestTemplate = restTemplateBuilder.errorHandler(restTemplateHandler).build()
}