package ru.rsreu.translate.api.translate.yandex.rest

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.ResponseErrorHandler
import ru.rsreu.translate.api.translate.exception.TranslateServiceException
import ru.rsreu.translate.api.translate.yandex.configuration.YandexTranslateServiceConfiguration

@Component
class YandexTranslateServiceRestClientHandler(
    private val config: YandexTranslateServiceConfiguration
) : ResponseErrorHandler {
    override fun hasError(response: ClientHttpResponse) =
        with(response.statusCode.series()) { this == HttpStatus.Series.CLIENT_ERROR || this == HttpStatus.Series.SERVER_ERROR }

    @OptIn(ExperimentalSerializationApi::class)
    override fun handleError(response: ClientHttpResponse) = throw TranslateServiceException(
        config.serviceKey, Json.decodeFromStream<YandexTranslateServiceExceptionInfo>(response.body)
    )
}