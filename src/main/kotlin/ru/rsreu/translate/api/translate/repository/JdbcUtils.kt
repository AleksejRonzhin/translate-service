package ru.rsreu.translate.api.translate.repository

import java.sql.Connection
import java.sql.ResultSet

fun <T> Connection.executeInTransaction(lambda: () -> T): T {
    autoCommit = false
    return runCatching {
        val result = lambda()
        commit()
        result
    }.onFailure {
        run {
            rollback()
            throw it
        }
    }.getOrThrow()
}

fun <T> ResultSet.convert(converter: (ResultSet) -> T): Collection<T> {
    val result = mutableListOf<T>()
    while (next()) {
        result.add(converter(this))
    }
    return result
}