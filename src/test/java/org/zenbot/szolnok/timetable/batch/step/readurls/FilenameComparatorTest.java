package org.zenbot.szolnok.timetable.batch.step.readurls;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class FilenameComparatorTest {

    private FilenameComparator testSubject;
    private File file1;
    private File file2;

    @Before
    public void setUp() {
        testSubject = new FilenameComparator();
        file1 = mock(File.class);
        file2 = mock(File.class);
    }

    @Test
    public void compareWithBiggerNumber() {
        // GIVEN
        given(file1.getName()).willReturn("24");
        given(file2.getName()).willReturn("6");

        // WHEN
        int result = testSubject.compare(file1, file2);

        // THEN
        assertThat(result).isPositive();
    }

    @Test
    public void compareWithSameNumberOneWithChar() {
        // GIVEN
        given(file1.getName()).willReturn("24");
        given(file2.getName()).willReturn("24A");

        // WHEN
        int result = testSubject.compare(file1, file2);

        // THEN
        assertThat(result).isNegative();
    }

    @Test
    public void compareWithSameNumberBothWithChar() {
        // GIVEN
        given(file1.getName()).willReturn("24A");
        given(file2.getName()).willReturn("24Y");

        // WHEN
        int result = testSubject.compare(file1, file2);

        // THEN
        assertThat(result).isNegative();
    }

    @Test
    public void compareEqualFileNames() {
        // GIVEN
        given(file1.getName()).willReturn("24A");
        given(file2.getName()).willReturn("24A");

        // WHEN
        int result = testSubject.compare(file1, file2);

        // THEN
        assertThat(result).isZero();
    }



}