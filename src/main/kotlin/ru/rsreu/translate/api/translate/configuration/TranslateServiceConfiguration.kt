package ru.rsreu.translate.api.translate.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.rsreu.translate.api.translate.service.utils.Separators

@Configuration
class TranslateServiceConfiguration(
    @Value("\${application.translation.word_separators}") private val separatorString: String
) {
    @Bean
    fun separators() = Separators(separatorString.toSet())
}