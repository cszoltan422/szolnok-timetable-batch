package org.zenbot.szolnok.timetable.backend.api.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("hibernate.properties")
class HibernateProperties {
    lateinit var showSql: String
    lateinit var ddlAuto: String
    lateinit var dialect: String
}