package ru.rsreu.translate.api.translate.repository

import org.springframework.stereotype.Repository
import ru.rsreu.translate.api.translate.model.TranslateRequest
import ru.rsreu.translate.api.translate.model.WordTranslation
import ru.rsreu.translate.configuration.DatabaseConfiguration
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import javax.sql.DataSource

@Repository
class TranslateRequestRepository(
    private val dataSource: DataSource, private val config: DatabaseConfiguration
) {
    fun create(translateRequest: TranslateRequest) {
        val connection = dataSource.connection
        connection.use {
            connection.executeInTransaction {
                val preparedStatement = connection.prepareStatement(
                    config.createRequestQuery, Statement.RETURN_GENERATED_KEYS
                )
                preparedStatement.use {
                    preparedStatement.setTranslateRequest(translateRequest)
                    preparedStatement.execute()
                    val resultSet = it.generatedKeys
                    val id = resultSet.use {
                        resultSet.next()
                        resultSet.getLong(1)
                    }
                    createTranslations(id, translateRequest.translations)
                }
            }
        }
    }

    private fun createTranslations(id: Long, translations: Collection<WordTranslation>) {
        val connection = dataSource.connection
        connection.use {
            val preparedStatement = connection.prepareStatement(config.createTranslationQuery)
            preparedStatement.use {
                preparedStatement.setLong(1, id)
                translations.forEach {
                    preparedStatement.setString(2, it.word)
                    preparedStatement.setString(3, it.translatedWord)
                    preparedStatement.addBatch()
                }
                preparedStatement.executeBatch()
            }
        }
    }

    private fun PreparedStatement.setTranslateRequest(translateRequest: TranslateRequest) = translateRequest.let {
        setTimestamp(1, it.date)
        setString(2, it.sourceLanguageCode)
        setString(3, it.targetLanguageCode)
        setString(4, it.inputText)
        setString(5, it.outputText)
        setString(6, it.ip)
        setString(7, it.translateServiceKey)
    }

    fun getAll(): Collection<TranslateRequest> {
        val connection = dataSource.connection
        return connection.use {
            val statement = connection.createStatement()
            statement.use {
                val resultSet = statement.executeQuery(config.getAllRequestsQuery)
                resultSet.use {
                    resultSet.convert(::convertResultSetToTranslateRequest)
                }
            }
        }
    }

    private fun convertResultSetToTranslateRequest(resultSet: ResultSet): TranslateRequest = with(resultSet) {
        val columns = config.translateRequestTableColumns
        val id = getLong(columns.id)
        return TranslateRequest(
            id = id,
            date = getTimestamp(columns.date),
            sourceLanguageCode = getString(columns.source),
            targetLanguageCode = getString(columns.target),
            inputText = getString(columns.input),
            outputText = getString(columns.output),
            ip = getString(columns.ip),
            translations = getTranslations(id),
            translateServiceKey = getString(columns.serviceKey)
        )
    }

    private fun getTranslations(requestId: Long): Collection<WordTranslation> {
        val connection = dataSource.connection
        return connection.use {
            val statement = connection.prepareStatement(config.getTranslationsByRequestIdQuery)
            statement.use {
                statement.setLong(1, requestId)
                val resultSet = statement.executeQuery()
                resultSet.use {
                    resultSet.convert(::convertResultSetToTranslation)
                }
            }
        }
    }

    private fun convertResultSetToTranslation(resultSet: ResultSet): WordTranslation = with(resultSet) {
        val columns = config.wordTranslationsTableColumns
        return WordTranslation(
            id = getLong(columns.id), word = getString(columns.word), translatedWord = getString(columns.translatedWord)
        )
    }

    fun getPerfectTranslationOrNull(source: String?, target: String, word: String, key: String): String? {
        val connection = dataSource.connection
        return connection.use {
            val statement = connection.prepareStatement(config.getPerfectTranslation)
            statement.use {
                statement.setPerfectTranslation(source, target, word, key)
                val resultSet = statement.executeQuery()
                resultSet.use {
                    if (resultSet.next()) convertResultSetToTranslation(resultSet).translatedWord else null
                }
            }
        }
    }

    private fun PreparedStatement.setPerfectTranslation(source: String?, target: String, word: String, key: String) {
        setString(1, word)
        setString(2, source)
        setString(3, target)
        setString(4, key)
    }
}