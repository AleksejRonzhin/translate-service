package ru.rsreu.translate.api.translate.yandex

import org.springframework.http.HttpEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity
import ru.rsreu.translate.api.translate.TranslateService
import ru.rsreu.translate.api.translate.yandex.dto.YandexTranslateRequest
import ru.rsreu.translate.api.translate.yandex.dto.YandexTranslateResponse
import ru.rsreu.translate.configuration.YandexTranslationServiceConfiguration

@Service
class YandexTranslateService(
    val restTemplate: RestTemplate,
    val config: YandexTranslationServiceConfiguration
) : TranslateService {
    override fun translate(source: String?, target: String, texts: List<String>): String {
        val request = YandexTranslateRequest(source, target, texts)
        val entity = HttpEntity<YandexTranslateRequest>(request, config.headers())
        val response = restTemplate.postForEntity<YandexTranslateResponse>(
            config.url + config.translateUrlPart, entity
        )
        return response.body?.translations?.first()?.text ?: throw Exception() // TODO
    }

}