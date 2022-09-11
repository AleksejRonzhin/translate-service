package ru.rsreu.translate.api.translate.service

import mu.KLogger
import org.springframework.stereotype.Service
import ru.rsreu.translate.api.translate.model.TranslateRequest
import ru.rsreu.translate.api.translate.repository.TranslateRequestRepository
import java.sql.Timestamp

@Service
class TranslateRequestService(
    private val translateService: TranslateService,
    private val repo: TranslateRequestRepository,
    private val logger: KLogger,
    private val separators: Separators<Char>
) {
    fun getRequests() = repo.getAll()

    fun create(
        sourceLanguageCode: String?, targetLanguageCode: String, text: String, date: Timestamp, ip: String
    ): TranslateRequest {

        val wordTranslator = { word: String ->
            translateWord(sourceLanguageCode, targetLanguageCode, word).also {
                logger.info("Translated $word to $it")
            }
        }

        logger.info("Started translation")
        val (translatedText, translations) = TextTranslatorByWords(wordTranslator, separators).translate(text)
        logger.info("Finished translation")

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

    private fun translateWord(source: String?, target: String, word: String) =
        repo.getPerfectTranslationOrNull(source, target, word, translateService.key) ?: run {
            logger.info { "Go to Yandex for word '$word'" }
            translateService.translate(source, target, listOf(word))
        }
}