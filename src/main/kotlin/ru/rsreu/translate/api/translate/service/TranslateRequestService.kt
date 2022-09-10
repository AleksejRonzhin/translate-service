package ru.rsreu.translate.api.translate.service

import org.springframework.stereotype.Service
import ru.rsreu.translate.api.translate.model.TranslateRequest
import ru.rsreu.translate.api.translate.repository.TranslateRequestRepository
import java.sql.Timestamp

@Service
class TranslateRequestService(
    private val translateService: TranslateService, private val repo: TranslateRequestRepository
) {
    fun getRequests() = repo.getAll()

    fun create(
        sourceLanguageCode: String?, targetLanguageCode: String, text: String, date: Timestamp, ip: String
    ): TranslateRequest {
        val wordTranslator = { word: String ->
            translateWord(sourceLanguageCode, targetLanguageCode, word)
        }
        val (translatedText, translations) = TextTranslatorByWords(wordTranslator).translate(text)
        return TranslateRequest(
            date = date,
            ip = ip,
            sourceLanguageCode = sourceLanguageCode,
            targetLanguageCode = targetLanguageCode,
            inputText = text,
            outputText = translatedText,
            translations = translations
        ).also { repo.create(it) }
    }

    private fun translateWord(source: String?, target: String, word: String) =
        translateService.translate(source, target, listOf(word))
}