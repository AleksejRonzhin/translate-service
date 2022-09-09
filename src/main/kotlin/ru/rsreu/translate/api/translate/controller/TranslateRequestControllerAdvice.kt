package ru.rsreu.translate.api.translate.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import ru.rsreu.translate.api.translate.yandex.exception.YandexServiceException

@ControllerAdvice(basePackageClasses = [TranslateRequestController::class])
class TranslateRequestControllerAdvice {
    @ExceptionHandler(YandexServiceException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleYandexServiceNotAvailableException(): ResponseEntity<ErrorResponseBody> =
        ResponseEntity(ErrorResponseBody("Ошибка яндекса"), HttpStatus.INTERNAL_SERVER_ERROR)
}

data class ErrorResponseBody(
    val message: String
)