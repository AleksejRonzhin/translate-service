package ru.rsreu.translate.api.translate.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import ru.rsreu.translate.api.translate.exception.TranslateServiceException
import ru.rsreu.translate.api.translate.exception.TranslateServiceExceptionInfo

@ControllerAdvice(basePackageClasses = [TranslateRequestController::class])
class TranslateRequestControllerAdvice {
    @ExceptionHandler(TranslateServiceException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleTranslateServiceException(exception: TranslateServiceException): ResponseEntity<ExceptionResponseBody> {
        return ResponseEntity(
            ExceptionResponseBody(exception.service, exception.info),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}

data class ExceptionResponseBody(
    val service: String,
    val info: TranslateServiceExceptionInfo
)

