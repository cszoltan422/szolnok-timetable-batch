package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor

import org.assertj.core.api.Assertions.assertThat
import org.jsoup.nodes.Document
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.anyString
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.zenbot.szolnok.timetable.backend.batch.utils.common.service.JsoupDocumentService

class UrlResourceToDocumentJsoupProcessorTest {

    @InjectMocks
    private lateinit var testSubject: UrlResourceToDocumentJsoupProcessor

    @Mock
    private lateinit var jsoupDocumentService: JsoupDocumentService

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `process should the service`() {
        // GIVEN
        val document = mock(Document::class.java)
        given(jsoupDocumentService.getDocument(anyString())).willReturn(document)

        // WHEN
        val result = testSubject.process("url")

        // THEN
        verify(jsoupDocumentService).getDocument("url")
        assertThat(result)
                .isNotNull()
                .isSameAs(document)
    }
}
