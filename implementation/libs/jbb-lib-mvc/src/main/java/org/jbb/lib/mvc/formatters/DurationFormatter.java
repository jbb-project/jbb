package org.jbb.lib.mvc.formatters;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.commons.lang3.Validate;
import org.jbb.lib.mvc.properties.MvcProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Duration;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class DurationFormatter  {

    private final MvcProperties mvcProperties;

    @Autowired
    public DurationFormatter(MvcProperties mvcProperties){
        this.mvcProperties=mvcProperties;
    }

    public Duration parse(long milliseconds) {
        return Duration.ofMillis(milliseconds);
    }

    public String print(Duration duration) {
        return DurationFormatUtils.formatDuration(duration.toMillis(),mvcProperties.durationFormatPattern());
    }

    public DateTimeFormatter getCurrentDateTimeFormatter() {
        return DateTimeFormatter.ofPattern(mvcProperties.durationFormatPattern());
    }

    public void setPattern(String pattern) {
        Validate.notBlank(pattern);
        DateTimeFormatter.ofPattern(pattern);
        mvcProperties.setProperty(MvcProperties.DURATION_FORMAT_KEY, pattern);
    }

    public String getCurrentPattern() {
        return mvcProperties.durationFormatPattern();
    }
}
