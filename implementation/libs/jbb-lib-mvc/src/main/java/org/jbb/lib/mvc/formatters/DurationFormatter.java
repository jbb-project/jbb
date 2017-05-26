package org.jbb.lib.mvc.formatters;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.commons.lang3.Validate;
import org.jbb.lib.mvc.properties.MvcProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class DurationFormatter implements Formatter<Duration> {

    private final MvcProperties mvcProperties;

    @Autowired
    public DurationFormatter(MvcProperties mvcProperties) {
        this.mvcProperties = mvcProperties;
    }

    public void setPattern(String pattern) {
        Validate.notBlank(pattern);
        DateTimeFormatter.ofPattern(pattern);
        mvcProperties.setProperty(MvcProperties.DURATION_FORMAT_KEY, pattern);
    }

    @Override
    public Duration parse(String timeAsString, Locale locale) {
        throw new UnsupportedOperationException("This method is not implemented yet!");
    }

    @Override
    public String print(Duration duration, Locale locale) {
        return DurationFormatUtils.formatDuration(duration.toMillis(),mvcProperties.durationFormatPattern());
    }
}
