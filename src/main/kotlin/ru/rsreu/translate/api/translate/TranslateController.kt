package ru.rsreu.translate.api.translate

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.rsreu.translate.api.translate.dto.TranslateRequest
import ru.rsreu.translate.api.translate.dto.TranslateResponse

@RestController
class TranslateController(
    val service: TranslateService
) {
    @GetMapping("/translate_requests")
    fun getTranslateRequest() = service.getAll()

    @PostMapping("/translate")
    fun translate(@RequestBody requestBody: TranslateRequest): TranslateResponse {
        val result = service.translate(requestBody.source, requestBody.target, requestBody.text)
        return TranslateResponse(result)
    }
}