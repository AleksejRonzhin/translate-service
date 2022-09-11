package ru.rsreu.translate.api.translate.dto

import ru.rsreu.translate.api.translate.model.TranslateRequest

data class GetTranslateRequestsResponseBody(
    val requests: Collection<TranslateRequest>
)
