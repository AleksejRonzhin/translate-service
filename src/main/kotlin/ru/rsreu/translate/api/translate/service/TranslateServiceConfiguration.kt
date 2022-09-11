package ru.rsreu.translate.api.translate.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TranslateServiceConfiguration(
    @Value("\${application.translation.word_separators}") private val separatorString: String
) {
    @Bean
    fun separators() = Separators(separatorString.toSet())
}