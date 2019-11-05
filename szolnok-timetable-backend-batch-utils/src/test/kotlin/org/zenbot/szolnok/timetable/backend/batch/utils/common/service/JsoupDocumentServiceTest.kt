package org.zenbot.szolnok.timetable.backend.batch.utils.common.service

import org.apache.commons.io.IOUtils
import org.assertj.core.api.Assertions.assertThat
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.powermock.api.mockito.PowerMockito
import org.powermock.api.mockito.PowerMockito.`when`
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.io.IOException
import java.net.URI

@RunWith(PowerMockRunner::class)
@PrepareForTest(Jsoup::class, IOUtils::class)
class JsoupDocumentServiceTest {

    private lateinit var testSubject: JsoupDocumentService

    private var uri: URI = URI.create("URL")

    @Before
    fun setUp() {
        testSubject = JsoupDocumentService()

        PowerMockito.mockStatic(Jsoup::class.java)
        PowerMockito.mockStatic(IOUtils::class.java)
    }

    @Test(expected = IllegalStateException::class)
    fun `getDocument should throw IllegalStateException if retries fail 4 times`() {
        // GIVEN
        `when`(IOUtils.toString(any(URI::class.java), anyString())).thenThrow(IOException::class.java)

        // WHEN
        testSubject.getDocument("URL")

        // THEN exception is thrown...
    }

    @Test
    fun `getDocument should return the document if Jsoup was able to fetch`() {
        // GIVEN
        val document = mock(Document::class.java)
        val htmlString = "HTML"
        `when`(IOUtils.toString(any(URI::class.java), anyString()))
                .thenThrow(IOException::class.java)
                .thenReturn(htmlString)
        `when`(Jsoup.parse(anyString())).thenReturn(document)

        // WHEN
        val result = testSubject.getDocument("URL")

        // THEN
        PowerMockito.verifyStatic(IOUtils::class.java, Mockito.times(2))
        IOUtils.toString(uri, "UTF-8")

        PowerMockito.verifyStatic(Jsoup::class.java)
        Jsoup.parse(htmlString)

        assertThat(result)
                .isNotNull()
                .isSameAs(document)
    }
}
