package org.zenbot.szolnok.timetable.backend.batch.utils.common.service

import org.assertj.core.api.Assertions.assertThat
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.powermock.api.mockito.PowerMockito
import org.powermock.api.mockito.PowerMockito.`when`
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.io.IOException

@RunWith(PowerMockRunner::class)
@PrepareForTest(Jsoup::class)
class JsoupDocumentServiceTest {

    private lateinit var testSubject: JsoupDocumentService

    @Before
    fun setUp() {
        testSubject = JsoupDocumentService()
        PowerMockito.mockStatic(Jsoup::class.java)
    }

    @Test(expected = IllegalStateException::class)
    fun `getDocument should throw IllegalStateException if retries fail 4 times`() {
        // GIVEN
        val connection = mock(Connection::class.java)
        `when`(Jsoup.connect(anyString())).thenReturn(connection)
        given(connection.get()).willThrow(IOException::class.java)

        // WHEN
        testSubject.getDocument("URL")

        // THEN exception is thrown...
    }

    @Test
    fun `getDocument should return the document if Jsoup was able to fetch`() {
        // GIVEN
        val connection = mock(Connection::class.java)
        val document = mock(Document::class.java)
        `when`(Jsoup.connect(anyString())).thenReturn(connection)
        given(connection.get())
                .willThrow(IOException::class.java)
                .willReturn(document)

        // WHEN
        val result = testSubject.getDocument("URL")

        // THEN
        PowerMockito.verifyStatic(Jsoup::class.java, Mockito.times(2))
        Jsoup.connect("URL")
        Mockito.verify(connection, Mockito.times(2)).get()

        assertThat(result)
                .isNotNull()
                .isSameAs(document)
    }
}