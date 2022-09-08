package ru.rsreu.translate.api.translate.yandex

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity
import ru.rsreu.translate.api.translate.TranslateRequestRepository
import ru.rsreu.translate.api.translate.TranslateService
import ru.rsreu.translate.api.translate.model.TranslateRequestInfo
import ru.rsreu.translate.api.translate.model.Translation
import ru.rsreu.translate.api.translate.yandex.dto.YandexTranslateRequest
import ru.rsreu.translate.api.translate.yandex.dto.YandexTranslateResponse
import ru.rsreu.translate.configuration.YandexTranslationServiceConfiguration
import java.sql.Timestamp
import java.time.Instant
import java.util.stream.Collectors.toList

@Service
class YandexTranslateService(
    val restTemplate: RestTemplate,
    val config: YandexTranslationServiceConfiguration,
    val headers: HttpHeaders,
    val repo: TranslateRequestRepository
) : TranslateService {
    override fun getAll(): List<TranslateRequestInfo> = repo.getAll()

    override fun translate(source: String?, target: String, text: String): String {
        val translations = getTranslations(source, target, text)
        val translatedText = getTranslationText(translations)
        repo.create(
            TranslateRequestInfo(
                date = Timestamp.from(Instant.now()),
                sourceLanguageCode = source,
                targetLanguageCode = target,
                inputText = text,
                outputText = translatedText,
                ip = "ip",
                translations = translations
            )
        )
        return translatedText
    }

    private fun getTranslations(source: String?, target: String, text: String) =
        text.split(" ").stream().parallel().map { word ->
            Translation(word = word, translatedWord = translateWord(source, target, word))
        }.collect(toList())

    private fun getTranslationText(translations: List<Translation>) =
        translations.joinToString(" ") { it.translatedWord }

    private fun translateWord(source: String?, target: String, word: String): String {
        val request = YandexTranslateRequest(source, target, listOf(word))
        val entity = HttpEntity<YandexTranslateRequest>(request, headers)
        val response = restTemplate.postForEntity<YandexTranslateResponse>(
            config.url + config.translateUrlPart, entity
        )
        return response.body?.translations?.first()?.text ?: config.translateErrorText
    }
}