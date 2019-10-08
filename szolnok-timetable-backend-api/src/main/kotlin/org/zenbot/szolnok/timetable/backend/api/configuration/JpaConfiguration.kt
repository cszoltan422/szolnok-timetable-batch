package org.zenbot.szolnok.timetable.backend.api.configuration

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.zenbot.szolnok.timetable.backend.api.configuration.properties.HibernateProperties
import java.util.Properties
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = arrayOf("org.zenbot.szolnok.timetable.backend.repository"))
@EnableConfigurationProperties(HibernateProperties::class)
class JpaConfiguration(
        private val hibernateProperties: HibernateProperties
) {

    @Bean
    fun entityManagerFactory(@Qualifier("dataSource") dataSource: DataSource): LocalContainerEntityManagerFactoryBean {
        val em = LocalContainerEntityManagerFactoryBean()
        em.dataSource = dataSource
        em.setPackagesToScan(*arrayOf("org.zenbot.szolnok.timetable.backend.domain.entity"))

        val vendorAdapter = HibernateJpaVendorAdapter()
        em.jpaVendorAdapter = vendorAdapter
        em.setJpaProperties(additionalProperties())

        return em
    }

    fun additionalProperties(): Properties {
        val properties = Properties()
        properties.setProperty("hibernate.show_sql", hibernateProperties.showSql)
        properties.setProperty("hibernate.hbm2ddl.auto", hibernateProperties.ddlAuto)
        properties.setProperty("hibernate.dialect", hibernateProperties.dialect)
        return properties
    }

    @Bean
    fun transactionManager(emf: EntityManagerFactory): JpaTransactionManager {
        return JpaTransactionManager(emf)
    }
}