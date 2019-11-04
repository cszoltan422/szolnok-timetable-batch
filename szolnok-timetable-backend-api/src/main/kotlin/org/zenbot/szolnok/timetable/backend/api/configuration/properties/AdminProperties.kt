package org.zenbot.szolnok.timetable.backend.api.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("admin")
data class AdminProperties(
    var user: AdminUserProperties = AdminUserProperties(),
    var application: AdminApplicationrProperties = AdminApplicationrProperties()
)
