package org.zenbot.szolnok.timetable.backend.batch.utils.common.configuration

import org.springframework.batch.core.JobExecutionListener
import org.springframework.batch.core.listener.CompositeJobExecutionListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.listener.BatchJobExecutionListener
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.listener.RemoveBusRoutesExecutionListener

@Configuration
class CompositeJobExecutionListenerConfiguration(
    private val removeBusRoutesExecutionListener: RemoveBusRoutesExecutionListener,
    private val batchJobExecutionListener: BatchJobExecutionListener
) {

    @Bean
    fun compositeListener(): JobExecutionListener {
        val compositeJobExecutionListener = CompositeJobExecutionListener()
        val listeners = ArrayList<JobExecutionListener>()
        listeners.add(0, batchJobExecutionListener)
        listeners.add(1, removeBusRoutesExecutionListener)
        compositeJobExecutionListener.setListeners(listeners)
        return compositeJobExecutionListener
    }
}
