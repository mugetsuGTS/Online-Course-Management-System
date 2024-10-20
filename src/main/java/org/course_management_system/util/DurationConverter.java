package org.course_management_system.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Duration;

@Converter(autoApply = true)
public class DurationConverter implements AttributeConverter<Duration, Long> {

    @Override
    public Long convertToDatabaseColumn(Duration duration) {
        return (duration == null ? null : duration.toMinutes());
    }

    @Override
    public Duration convertToEntityAttribute(Long dbData) {
        return (dbData == null ? null : Duration.ofMinutes(dbData));  // Convert back to Duration
    }
}
