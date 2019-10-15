package org.zenbot.szolnok.timetable.backend.batch.utils.common.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class StringCleanerTest {

    private lateinit var testSubject: StringCleaner

    @Before
    fun setUp() {
        testSubject = StringCleaner()
    }

    @Test
    fun `clean should clean the string from special characters, trim the string and remove multiple whitespaces`() {
        // GIVEN

        // WHEN
        val result = testSubject.clean("  û  õ  ô  ")

        // THEN
        assertThat(result).isNotNull()
                .isEqualTo("ű ő ő")
    }
}
