package org.zenbot.szolnok.timetable.backend.api.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("admin.user")
class AdminUserProperties {
    lateinit var username: String
    lateinit var password: String
}
