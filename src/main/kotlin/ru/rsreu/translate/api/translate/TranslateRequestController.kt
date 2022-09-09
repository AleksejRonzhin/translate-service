package ru.rsreu.translate.api.translate

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.rsreu.translate.api.translate.dto.TranslateRequestBody
import ru.rsreu.translate.api.translate.dto.TranslateResponseBody

@RestController
class TranslateRequestController(
    private val service: TranslateRequestService
) {
    @GetMapping("/translate_requests")
    fun getTranslateRequest() = service.getRequests()

    @PostMapping("/translate")
    fun translate(@RequestBody requestBody: TranslateRequestBody): TranslateResponseBody {
        val translatedText = service.translate(requestBody.source, requestBody.target, requestBody.text)
        return TranslateResponseBody(translatedText)
    }
}