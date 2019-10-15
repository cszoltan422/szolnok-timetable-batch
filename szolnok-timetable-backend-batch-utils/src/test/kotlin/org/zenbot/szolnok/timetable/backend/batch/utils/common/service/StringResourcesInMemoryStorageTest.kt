package org.zenbot.szolnok.timetable.backend.batch.utils.common.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class StringResourcesInMemoryStorageTest {

    private lateinit var testSubject: StringResourcesInMemoryStorage

    @Before
    fun setUp() {
        testSubject = StringResourcesInMemoryStorage()
    }

    @Test
    fun `addUrl should add to the list and get should rmeove the first`() {
        // GIVEN
        val url1 = "URL1"
        val url2 = "URL2"
        testSubject.addUrl(null)
        testSubject.addUrl(url1)
        testSubject.addUrl(url2)

        // WHEN
        val get1 = testSubject.get()
        val get2 = testSubject.get()
        val get3 = testSubject.get()

        // THEN
        assertThat(get1).isNotNull().isSameAs(url1)
        assertThat(get2).isNotNull().isSameAs(url2)
        assertThat(get3).isNull()
    }
}