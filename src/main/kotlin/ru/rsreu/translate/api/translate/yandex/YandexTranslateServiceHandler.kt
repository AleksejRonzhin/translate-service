package ru.rsreu.translate.api.translate.yandex

import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.ResponseErrorHandler
import ru.rsreu.translate.api.translate.yandex.exception.YandexServiceException

@Component
class YandexTranslateServiceHandler : ResponseErrorHandler {
    override fun hasError(response: ClientHttpResponse) =
        with(response.statusCode.series()) { this == HttpStatus.Series.CLIENT_ERROR || this == HttpStatus.Series.SERVER_ERROR }

    override fun handleError(response: ClientHttpResponse) {
        println(response.statusCode)
        // TODO
        throw YandexServiceException()
    }
}