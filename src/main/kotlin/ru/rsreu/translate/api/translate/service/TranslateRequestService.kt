package ru.rsreu.translate.api.translate.service

import org.springframework.stereotype.Service
import ru.rsreu.translate.api.translate.model.TranslateRequest
import ru.rsreu.translate.api.translate.repository.TranslateRequestRepository
import ru.rsreu.translate.api.translate.service.utils.Separators
import ru.rsreu.translate.api.translate.service.utils.TextTranslatorByWords
import java.sql.Timestamp

@Service
class TranslateRequestService(
    private val translateService: TranslateService,
    private val repo: TranslateRequestRepository,
    private val separators: Separators<Char>
) {
    fun getRequests() = repo.getAll()

    fun create(
        sourceLanguageCode: String?, targetLanguageCode: String, text: String, date: Timestamp, ip: String
    ): TranslateRequest {
        val wordTranslator = { words: List<String> ->
            translateWords(sourceLanguageCode, targetLanguageCode, words).toList()
        }

        val perfectTranslationChecker = { word: String ->
            repo.getPerfectTranslationOrNull(sourceLanguageCode, targetLanguageCode, word, translateService.key)
        }

        val (translatedText, translations) = TextTranslatorByWords(
            wordTranslator, perfectTranslationChecker, separators
        ).translate(text)


        return TranslateRequest(
            date = date,
            ip = ip,
            sourceLanguageCode = sourceLanguageCode,
            targetLanguageCode = targetLanguageCode,
            inputText = text,
            outputText = translatedText,
            translations = translations,
            translateServiceKey = translateService.key
        ).also { repo.create(it) }
    }

    private fun translateWords(source: String?, target: String, words: Collection<String>) = translateService.translate(
        source, target, words
    )
}