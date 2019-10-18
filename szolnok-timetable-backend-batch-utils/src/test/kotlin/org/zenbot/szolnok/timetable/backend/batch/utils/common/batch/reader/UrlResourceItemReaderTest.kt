package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.reader

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.zenbot.szolnok.timetable.backend.batch.utils.common.service.StringResourcesInMemoryStorage

class UrlResourceItemReaderTest {

    @InjectMocks
    private lateinit var testSubject: UrlResourceItemReader

    @Mock
    private lateinit var stringResourcesInMemoryStorage: StringResourcesInMemoryStorage

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `read should call the stringResourcesInMemoryStorage`() {
        // GIVEN
        val getResult = "result"
        given(stringResourcesInMemoryStorage.get()).willReturn(getResult)

        // WHEN
        val result = testSubject.read()

        // THEN
        verify(stringResourcesInMemoryStorage).get()

        assertThat(result)
                .isNotNull()
                .isSameAs(getResult)
    }
}
