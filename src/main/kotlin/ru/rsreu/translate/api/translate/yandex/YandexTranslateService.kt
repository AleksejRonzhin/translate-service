package ru.rsreu.translate.api.translate.yandex

import org.springframework.http.HttpEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.postForEntity
import ru.rsreu.translate.api.translate.service.TranslateService
import ru.rsreu.translate.api.translate.yandex.configuration.YandexTranslateServiceConfiguration
import ru.rsreu.translate.api.translate.yandex.dto.YandexTranslateRequest
import ru.rsreu.translate.api.translate.yandex.dto.YandexTranslateResponse
import ru.rsreu.translate.api.translate.yandex.rest.YandexTranslateServiceHeaders
import ru.rsreu.translate.api.translate.yandex.rest.YandexTranslateServiceRestClient

@Service
class YandexTranslateService(
    private val config: YandexTranslateServiceConfiguration,
    private val restClient: YandexTranslateServiceRestClient,
    private val headers: YandexTranslateServiceHeaders
) : TranslateService {
    override val key: String = config.serviceKey

    override fun translate(source: String?, target: String, texts: Collection<String>): Collection<String> {
        val request = YandexTranslateRequest(source, target, texts)
        val entity = HttpEntity<YandexTranslateRequest>(request, headers)
        val response = restClient.postForEntity<YandexTranslateResponse>(
            config.url + config.translateUrlPart, entity
        )
        return response.body?.translations?.map { it.text } ?: texts
    }
}