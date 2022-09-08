package ru.rsreu.translate.api.translate

import org.springframework.stereotype.Repository
import ru.rsreu.translate.api.translate.model.TranslateRequestInfo
import ru.rsreu.translate.configuration.DatabaseConfiguration
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.sql.DataSource

@Repository
class TranslateRequestRepository(
    val dataSource: DataSource, val conf: DatabaseConfiguration
) {
    fun getAll(): List<TranslateRequestInfo> {
        val statement = dataSource.connection.createStatement()
        val resultSet = statement.executeQuery(conf.getAllRequestsQuery)
        val requests = mutableListOf<TranslateRequestInfo>()
        with(resultSet) {
            while (next()) {
                requests.add(convertToRequestInfo())
            }
        }
        return requests
    }

    fun create(requestInfo: TranslateRequestInfo) {
        val preparedStatement = dataSource.connection.prepareStatement(conf.createRequestQuery)
        preparedStatement.setTranslateRequestInfo(requestInfo)
        preparedStatement.executeUpdate()
    }

    fun PreparedStatement.setTranslateRequestInfo(requestInfo: TranslateRequestInfo) = requestInfo.let {
        setTimestamp(1, it.date)
        setString(2, it.sourceLanguageCode)
        setString(3, it.targetLanguageCode)
        setString(4, it.inputText)
        setString(5, it.outputText)
        setString(6, it.ip)
    }

    fun ResultSet.convertToRequestInfo() = TranslateRequestInfo(
        id = getLong("id"),
        date = getTimestamp("request_date"),
        sourceLanguageCode = getString("source_language_code"),
        targetLanguageCode = getString("target_language_code"),
        inputText = getString("input_text"),
        outputText = getString("output_text"),
        ip = getString("ip")
    )
}