package ru.rsreu.translate.api.translate

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.rsreu.translate.api.translate.dto.TranslateRequest
import ru.rsreu.translate.api.translate.dto.TranslateResponse

@RestController
class TranslateController(
    val service: TranslateService
) {

    @PostMapping("/translate")
    fun translate(@RequestBody request: TranslateRequest): TranslateResponse {

        val result = service.translate(request.source, request.target, request.text)
        return TranslateResponse(result)
    }


}