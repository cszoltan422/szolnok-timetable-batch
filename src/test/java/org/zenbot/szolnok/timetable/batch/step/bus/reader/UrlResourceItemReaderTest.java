package org.zenbot.szolnok.timetable.batch.step.bus.reader;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.zenbot.szolnok.timetable.batch.step.readurls.StringResourcesInMemoryStorage;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class UrlResourceItemReaderTest {

    @Mock
    private StringResourcesInMemoryStorage stringResourcesInMemoryStorage;

    private UrlResourceItemReader testSubject;

    @Before
    public void setUp() {
        initMocks(this);
        testSubject = new UrlResourceItemReader(stringResourcesInMemoryStorage);
    }

    @Test
    public void readShouldCallTheStringResourcesInMemoryStorageEachTime() {
        // GIVEN


        // WHEN
        testSubject.read();
        testSubject.read();

        //THEN
        verify(stringResourcesInMemoryStorage, times(2)).get();
    }

}