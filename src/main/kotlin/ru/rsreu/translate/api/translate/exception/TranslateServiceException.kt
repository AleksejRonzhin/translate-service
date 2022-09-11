package ru.rsreu.translate.api.translate.exception

open class TranslateServiceException(val service: String, val info: TranslateServiceExceptionInfo): Exception()

open class TranslateServiceExceptionInfo