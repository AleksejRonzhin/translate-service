package ru.rsreu.translate.api.translate.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.rsreu.translate.api.translate.dto.GetTranslateRequestsResponseBody
import ru.rsreu.translate.api.translate.dto.TranslateRequestBody
import ru.rsreu.translate.api.translate.dto.TranslateResponseBody
import ru.rsreu.translate.api.translate.service.TranslateRequestService
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/translate_request")
class TranslateRequestController(
    private val service: TranslateRequestService
) {
    @GetMapping("/all")
    fun getTranslateRequest() = ResponseEntity.ok(GetTranslateRequestsResponseBody(service.getRequests()))

    @PostMapping("/create")
    fun translate(@RequestBody requestBody: TranslateRequestBody, request: HttpServletRequest): ResponseEntity<TranslateResponseBody> {
        println(request.remoteAddr)

        val translatedText = service.translate(requestBody.source, requestBody.target, requestBody.text)
        return ResponseEntity.ok(TranslateResponseBody(translatedText))
    }
}