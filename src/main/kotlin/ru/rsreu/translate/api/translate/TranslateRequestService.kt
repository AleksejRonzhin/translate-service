package ru.rsreu.translate.api.translate

import org.springframework.stereotype.Service
import ru.rsreu.translate.api.translate.model.TranslateRequest
import ru.rsreu.translate.api.translate.model.WordTranslation
import java.sql.Timestamp
import java.time.Instant
import java.util.stream.Collectors

@Service
class TranslateRequestService(
    private val translateService: TranslateService,
    private val repo: TranslateRequestRepository
) {
    fun getRequests() = repo.getAll()

    fun translate(source: String?, target: String, text: String): String {
        val translations = getWordTranslations(source, target, text)
        val translatedText = formTranslationText(translations)
        repo.create(
            TranslateRequest(
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

    // TODO Не просто split, пустые строки надо скипать
    private fun getWordTranslations(source: String?, target: String, text: String) =
        text.split(" ").stream().parallel().map { word ->
            WordTranslation(word = word, translatedWord = translateWord(source, target, word))
        }.collect(Collectors.toList())

    private fun translateWord(source: String?, target: String, word: String) =
        translateService.translate(source, target, listOf(word))

    private fun formTranslationText(translations: List<WordTranslation>) =
        translations.joinToString(" ") { it.translatedWord }
}