package ru.rsreu.translate.api.translate.service.utils

class TextPartSequence(private val sequence: List<TextPart>) {
    companion object {
        fun createByText(text: String, separators: Separators<Char>): TextPartSequence {
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
        TextPart(if (it.isWord) replacer(it.text) else it.text, it.isWord)
    })

    override fun toString() = buildString { sequence.map { it.text }.forEach(::append) }

    fun getWords(): List<String> = sequence.filter { it.isWord }.map { it.text }
}

data class TextPart(
    val text: String, val isWord: Boolean
)

private fun String.takeWhile(startIndex: Int, predicate: (Char) -> Boolean): Pair<String, Int> =
    substring(startIndex).takeWhile(predicate).let {
        Pair(it, startIndex + it.length)
    }