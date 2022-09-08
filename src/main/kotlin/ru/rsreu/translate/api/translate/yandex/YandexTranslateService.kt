package ru.rsreu.translate.api.translate.yandex

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity
import ru.rsreu.translate.api.translate.TranslateService
import ru.rsreu.translate.api.translate.yandex.dto.YandexTranslateRequest
import ru.rsreu.translate.api.translate.yandex.dto.YandexTranslateResponse
import ru.rsreu.translate.configuration.YandexTranslationServiceConfiguration
import java.util.stream.Collectors.joining

@Service
class YandexTranslateService(
    val restTemplate: RestTemplate, val config: YandexTranslationServiceConfiguration, val headers: HttpHeaders
) : TranslateService {
    override fun translate(source: String?, target: String, text: String): String {
        return text.split(" ").stream().parallel()
            .map { word -> translateWord(source, target, word) }
            .collect(joining(" "))
    }

    private fun translateWord(source: String?, target: String, word: String): String {
        val request = YandexTranslateRequest(source, target, listOf(word))
        val entity = HttpEntity<YandexTranslateRequest>(request, headers)
        val response = restTemplate.postForEntity<YandexTranslateResponse>(
            config.url + config.translateUrlPart, entity
        )
        return response.body?.translations?.first()?.text ?: config.translateErrorText
    }
}