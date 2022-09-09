package ru.rsreu.translate.api.translate.service

import org.springframework.stereotype.Service
import ru.rsreu.translate.api.translate.model.TranslateRequest
import ru.rsreu.translate.api.translate.model.WordTranslation
import ru.rsreu.translate.api.translate.repository.TranslateRequestRepository
import java.util.stream.Collectors

@Service
class TranslateRequestService(
    private val translateService: TranslateService, private val repo: TranslateRequestRepository
) {
    fun getRequests() = repo.getAll()

    fun translate(translateRequest: TranslateRequest): String {
        val translations = getWordTranslations(
            translateRequest.sourceLanguageCode, translateRequest.targetLanguageCode, translateRequest.inputText
        )
        val translatedText = formTranslationText(translations)
        translateRequest.outputText = translatedText
        translateRequest.translations = translations
        repo.create(translateRequest)
        return translatedText
    }

    private fun getWordTranslations(source: String?, target: String, text: String) =
        text.split(" ").stream().parallel().map { word ->
            WordTranslation(word = word, translatedWord = translateWord(source, target, word))
        }.collect(Collectors.toList())

    private fun translateWord(source: String?, target: String, word: String) =
        translateService.translate(source, target, listOf(word))

    private fun formTranslationText(translations: List<WordTranslation>) =
        translations.joinToString(" ") { it.translatedWord }
}