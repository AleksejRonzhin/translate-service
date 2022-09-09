package ru.rsreu.translate.api.translate.yandex

import org.springframework.http.HttpEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.postForEntity
import ru.rsreu.translate.api.translate.service.TranslateService
import ru.rsreu.translate.api.translate.yandex.configuration.YandexTranslateServiceConfiguration
import ru.rsreu.translate.api.translate.yandex.dto.YandexTranslateRequest
import ru.rsreu.translate.api.translate.yandex.dto.YandexTranslateResponse
import ru.rsreu.translate.api.translate.yandex.exception.YandexServiceException

@Service
class YandexTranslateService(
    private val config: YandexTranslateServiceConfiguration
) : TranslateService {
    override fun translate(source: String?, target: String, texts: List<String>): String {
        val request = YandexTranslateRequest(source, target, texts)
        val entity = HttpEntity<YandexTranslateRequest>(request, config.headers())
        val response = config.restTemplate().postForEntity<YandexTranslateResponse>(
            config.url + config.translateUrlPart, entity
        )
        return response.body?.translations?.first()?.text ?: throw YandexServiceException()
    }
}