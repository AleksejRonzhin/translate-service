package ru.rsreu.translate.api.translate

import org.springframework.stereotype.Repository
import ru.rsreu.translate.api.translate.model.TranslateRequestInfo
import ru.rsreu.translate.api.translate.model.Translation
import ru.rsreu.translate.configuration.DatabaseConfiguration
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import javax.sql.DataSource

@Repository
class TranslateRequestRepository(
    val dataSource: DataSource, val config: DatabaseConfiguration
) {
    fun getAll(): List<TranslateRequestInfo> {
        val statement = dataSource.connection.createStatement()
        val resultSet = statement.executeQuery(config.getAllRequestsQuery)
        val requests = mutableListOf<TranslateRequestInfo>()
        with(resultSet) {
            while (next()) {
                requests.add(convertToRequestInfo())
            }
        }
        return requests
    }

    fun create(requestInfo: TranslateRequestInfo) {
        with(dataSource.connection.prepareStatement(config.createRequestQuery,
            Statement.RETURN_GENERATED_KEYS)) {
            setTranslateRequestInfo(requestInfo)
            execute()
            val id = generatedKeys.let {
                it.next()
                it.getLong(1)
            }
            createTranslations(id, requestInfo.translations)
        }
    }

    private fun createTranslations(id: Long, translations: List<Translation>) {
        with(dataSource.connection.prepareStatement(config.createTranslationQuery)) {
            setLong(1, id)
            translations.forEach {
                setString(2, it.word)
                setString(3, it.translatedWord)
                addBatch()
            }
            executeBatch()
        }
    }

    private fun PreparedStatement.setTranslateRequestInfo(requestInfo: TranslateRequestInfo) = requestInfo.let {
        setTimestamp(1, it.date)
        setString(2, it.sourceLanguageCode)
        setString(3, it.targetLanguageCode)
        setString(4, it.inputText)
        setString(5, it.outputText)
        setString(6, it.ip)
    }

    private fun ResultSet.convertToRequestInfo(): TranslateRequestInfo {
        val id = getLong("id")
        return TranslateRequestInfo(
            id = id,
            date = getTimestamp("request_date"),
            sourceLanguageCode = getString("source_language_code"),
            targetLanguageCode = getString("target_language_code"),
            inputText = getString("input_text"),
            outputText = getString("output_text"),
            ip = getString("ip"),
            translations = getTranslations(id)
        )
    }

    private fun getTranslations(requestId: Long): MutableList<Translation> {
        val statement = dataSource.connection.prepareStatement(config.getTranslationsByRequestIdQuery)
        statement.setLong(1, requestId)
        val resultSet = statement.executeQuery()
        val translations = mutableListOf<Translation>()
        with(resultSet) {
            while (next()) {
                translations.add(convertToTranslation())
            }
        }
        return translations
    }

    private fun ResultSet.convertToTranslation() = Translation(
        id = getLong("id"), word = getString("word"), translatedWord = getString("translated_word")
    )
}