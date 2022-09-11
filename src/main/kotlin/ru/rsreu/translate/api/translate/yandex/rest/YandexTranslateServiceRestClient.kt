package ru.rsreu.translate.api.translate.yandex.rest

import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class YandexTranslateServiceRestClient(
    handler: YandexTranslateServiceRestClientHandler
) : RestTemplate() {
    init {
        this.errorHandler = handler
    }
}