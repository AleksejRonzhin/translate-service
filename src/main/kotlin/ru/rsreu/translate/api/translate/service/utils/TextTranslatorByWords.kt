package ru.rsreu.translate.api.translate.service.utils

import ru.rsreu.translate.api.translate.model.WordTranslation
import java.util.stream.Collectors.toSet

class TextTranslatorByWords(
    private val translator: (String) -> String, private val separators: Separators<Char>
) {
    fun translate(text: String): Pair<String, Set<WordTranslation>> {
        val textPartSequence = TextPartSequence.createByText(text, separators)
        val wordSet = textPartSequence.getWords().toSet()
        val wordsTranslations = getWordTranslations(wordSet)
        val translatedText = textPartSequence.replaceWords { word ->
            wordsTranslations.find { it.word == word }?.translatedWord ?: word
        }.toString()
        return Pair(translatedText, wordsTranslations)
    }

    private fun getWordTranslations(words: Set<String>): Set<WordTranslation> {
        return words.stream().parallel().map { WordTranslation(word = it, translatedWord = translator(it)) }
            .collect(toSet())
    }

}