package org.jbb.lib.mvc.formatters;


import org.jbb.lib.mvc.properties.MvcProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.ParseException;
import java.time.Duration;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DurationFormatterTest {

    @Mock
    private MvcProperties propertiesMock;

    private DurationFormatter durationFormatter;

    @Before
    public void setUp() throws Exception {
        when(propertiesMock.durationFormatPattern()).thenReturn("HH:mm:ss");

        durationFormatter = new DurationFormatter(propertiesMock);
    }

    @Test
    public void whenDurationIsPassedThenShouldBeParse() throws ParseException {
        //given
        String oneDayAsMilliSeconds = "86400000";
        //when
        Duration oneDayAsDuration = durationFormatter.parse(oneDayAsMilliSeconds, Locale.getDefault());
        //then
        assertEquals(1,oneDayAsDuration.toDays());
    }

    @Test
    public void whenDurationIsPassedThenShouldBePrint(){
        //given
        Duration oneDayDuration = Duration.ofMillis(86400000L);
        //when
        String oneDayAsString = durationFormatter.print(oneDayDuration,Locale.getDefault());
        //then
        assertEquals("24:00:00",oneDayAsString);
    }

    @Test
    public void WhenSettingPatternInvokedThenPropertyIsUpdated() throws Exception {

        //given
        String newPattern = "dd/MM/yyyy HH:mm:ss";
        //when
        durationFormatter.setPattern(newPattern);
        //then
        verify(propertiesMock,times(1)).setProperty(anyString(),anyString());
    }
}
