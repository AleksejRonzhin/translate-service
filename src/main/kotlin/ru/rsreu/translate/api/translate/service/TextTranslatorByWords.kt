package ru.rsreu.translate.api.translate.service

import ru.rsreu.translate.api.translate.model.WordTranslation
import java.util.stream.Collectors.toList

class TextTranslatorByWords(
    private val translator: (String) -> String
) {

    fun translate(text: String): Pair<String, List<WordTranslation>> {
        val words = text.split(" ")
        val wordsTranslations = words.filter { it.isNotBlank() }.stream().parallel()
            .map { WordTranslation(word = it, translatedWord = translator(it)) }.collect(toList())
        val translatedText = wordsTranslations.joinToString(" ") { it.translatedWord }
        return Pair(translatedText, wordsTranslations)
    }
}