package org.zenbot.szolnok.timetable.admin.configuration

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.Properties
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = arrayOf("org.zenbot.szolnok.timetable.backend.repository"))
class JpaConfiguration {

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
        properties.setProperty("hibernate.show_sql", "true")
        properties.setProperty("hibernate.hbm2ddl.auto", "validate")
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect")
        return properties
    }

    @Bean
    fun transactionManager(emf: EntityManagerFactory): JpaTransactionManager {
        return JpaTransactionManager(emf)
    }


}