package ru.rsreu.translate.configuration

import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.rsreu.translate.api.translate.service.Separators

@Configuration
class ApplicationConfiguration {
    @Bean
    fun logger() = KotlinLogging.logger {}

    @Bean
    fun separators() = Separators(setOf(' ', '.', ',', '!', '?', '\t', '\n', ':'))
}