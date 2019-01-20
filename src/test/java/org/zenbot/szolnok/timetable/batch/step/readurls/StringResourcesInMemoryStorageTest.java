package org.zenbot.szolnok.timetable.batch.step.readurls;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringResourcesInMemoryStorageTest {

    private static final String URL_1 = "URL_1";
    private static final String URL_2 = "URL_2";
    private StringResourcesInMemoryStorage testSubject;

    @Before
    public void setUp() {
        testSubject = new StringResourcesInMemoryStorage();
    }

    @Test
    public void addNullUrlShouldNotBeAdded() {
        // GIVEN
        testSubject.addUrl(null);

        // WHEN
        String result = testSubject.get();

        // THEN
        assertThat(result)
                .isNull();
    }

    @Test
    public void getShouldReturnWithTheFirstThenRemoveThat() {
        // GIVEN
        testSubject.addUrl(URL_1);
        testSubject.addUrl(URL_2);

        // WHEN
        String first = testSubject.get();
        String second = testSubject.get();
        String third = testSubject.get();

        // THEN
        assertThat(first).isNotNull().isEqualTo(URL_1);
        assertThat(second).isNotNull().isEqualTo(URL_2);
        assertThat(third).isNull();
    }

}