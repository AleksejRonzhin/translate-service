package ru.rsreu.translate.api.translate.yandex.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.rsreu.translate.api.translate.yandex.rest.YandexTranslateServiceHeaders

@Configuration
data class YandexTranslateServiceConfiguration(
    @Value("\${yandex_translation_service.url}") val url: String,
    @Value("\${yandex_translation_service.api_key}") val apiKey: String,
    @Value("\${yandex_translation_service.api.translate}") val translateUrlPart: String,
    @Value("\${yandex_translation_service.api.translate_error}") val translateErrorText: String,
    @Value("\${yandex_translation_service.service_key}") val serviceKey: String,
    private val restTemplateBuilder: RestTemplateBuilder
) {
    @Bean
    fun headers() = YandexTranslateServiceHeaders().apply { add("Authorization", "Api-Key $apiKey") }
}