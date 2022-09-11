package ru.rsreu.translate.api.translate.service.utils

class TextPartSequence(private val sequence: List<TextPart>) {
    companion object {
        private val EMPTY_SEQUENCE = TextPartSequence(listOf())

        fun createByText(text: String, separators: Separators<Char>): TextPartSequence {
            if (text.isEmpty()) return EMPTY_SEQUENCE

            val list = mutableListOf<TextPart>()
            var partStartIndex = 0
            var nextWord = separators.contains(text[partStartIndex]).not()
            while (partStartIndex < text.length) {
                val predicate: (Char) -> Boolean =
                    { if (nextWord) !separators.contains(it) else separators.contains(it) }
                val (extractedTextPart, nextPartStartIndex) = text.takeWhile(partStartIndex, predicate)
                list.add(TextPart(extractedTextPart, nextWord))
                partStartIndex = nextPartStartIndex
                nextWord = nextWord.not()
            }
            return TextPartSequence(list)
        }
    }

    fun replaceWords(replacer: (String) -> String) = TextPartSequence(sequence.map {
        TextPart(if (it.isNeedReplace) replacer(it.text) else it.text, it.isNeedReplace)
    })

    override fun toString() = buildString { sequence.map { it.text }.forEach(::append) }

    fun getWords(): List<String> = sequence.filter { it.isNeedReplace }.map { it.text }
    fun replaceWordsWithCacheChecker(cacheChecker: (String) -> String?) = TextPartSequence(sequence.map { oldTextPart ->
        if (oldTextPart.isNeedReplace) cacheChecker(oldTextPart.text)?.let { cacheValue ->
            TextPart(cacheValue, false)
        } ?: oldTextPart
        else oldTextPart
    })
}

data class TextPart(
    val text: String, val isNeedReplace: Boolean
)

private fun String.takeWhile(startIndex: Int, predicate: (Char) -> Boolean): Pair<String, Int> =
    substring(startIndex).takeWhile(predicate).let {
        Pair(it, startIndex + it.length)
    }