package ru.rsreu.translate.api.translate.service.utils

import ru.rsreu.translate.api.translate.model.WordTranslation
import java.util.stream.Collectors.toSet

class TextTranslatorByWords(
    private val translator: (List<String>) -> List<String>,
    private val perfectTranslationChecker: (String) -> String?,
    private val separators: Separators<Char>
) {
    fun translate(text: String): Pair<String, Collection<WordTranslation>> {
        val textPartSequence =
            TextPartSequence.createByText(text, separators).replaceWordsWithCacheChecker(perfectTranslationChecker)
        val wordSet = textPartSequence.getWords().toSet()

        val wordsTranslations = getWordTranslations(wordSet)
        val translatedText = textPartSequence.replaceWords { word ->
            wordsTranslations.find { it.word == word }?.translatedWord ?: word
        }.toString()
        return Pair(translatedText, wordsTranslations)
    }

    private fun getWordTranslations(words: Collection<String>): Collection<WordTranslation> {
        if (words.isEmpty()) return listOf()
        val maxThreadCount = 10

        return words.chunked((words.size.toDouble() / maxThreadCount).ceil()).stream().parallel().map {
            val translatedWords = translator(it)
            val wordTranslations = mutableListOf<WordTranslation>()
            for (i in translatedWords.indices) {
                wordTranslations.add(WordTranslation(word = it[i], translatedWord = translatedWords[i]))
            }
            return@map wordTranslations
        }.flatMap { it.stream() }.collect(toSet())
    }
}

private fun Double.ceil() = kotlin.math.ceil(this).toInt()