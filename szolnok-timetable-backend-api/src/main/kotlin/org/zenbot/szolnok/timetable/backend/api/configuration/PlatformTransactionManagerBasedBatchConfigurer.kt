package org.zenbot.szolnok.timetable.backend.api.configuration

import org.springframework.batch.core.configuration.annotation.BatchConfigurer
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.SimpleJobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

class PlatformTransactionManagerBasedBatchConfigurer(
        private val embeddedDataSource: DataSource,
        private val transactionManager: PlatformTransactionManager
): BatchConfigurer {

    override fun getTransactionManager() = transactionManager

    override fun getJobRepository(): JobRepository {
        val factory = JobRepositoryFactoryBean()
        factory.setDataSource(embeddedDataSource)
        factory.transactionManager = transactionManager
        factory.afterPropertiesSet()
        return factory.getObject()
    }

    override fun getJobLauncher(): JobLauncher {
        val jobLauncher = SimpleJobLauncher()
        jobLauncher.setJobRepository(jobRepository)
        jobLauncher.afterPropertiesSet()
        return jobLauncher
    }

    override fun getJobExplorer(): JobExplorer {
        val jobExplorerFactoryBean = JobExplorerFactoryBean()
        jobExplorerFactoryBean.setDataSource(this.embeddedDataSource)
        jobExplorerFactoryBean.afterPropertiesSet()
        return jobExplorerFactoryBean.getObject()
    }
}