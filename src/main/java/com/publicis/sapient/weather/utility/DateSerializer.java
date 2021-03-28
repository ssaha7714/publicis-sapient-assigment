package com.publicis.sapient.weather.utility;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class DateSerializer extends JsonDeserializer<LocalDate> {
    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        try {
            final LocalDateTime localDateTime = LocalDateTime.parse(jsonParser.getText().trim(),
                    DateTimeFormatter.ofPattern(CommonConstants.DATE_TIME_FORMATTER));
            return localDateTime.toLocalDate();
        } catch (final Exception e) {
            log.error("Error while Getting value for {} value ::  {} {}", jsonParser.getCurrentName(),
                    jsonParser.getText(), e);
        }
        return null;
    }
}
