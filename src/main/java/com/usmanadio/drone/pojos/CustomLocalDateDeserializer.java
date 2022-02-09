package com.usmanadio.drone.pojos;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeParseException;

@Slf4j
public class CustomLocalDateDeserializer extends JsonDeserializer<Instant> {

    @Override
    public Instant deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        Instant localDate = null;
        if (Strings.isNotBlank(jsonParser.getText())) {
            try {
                String dateString = jsonParser.getText().trim();
                localDate = Instant.parse(dateString);
            } catch (IOException | DateTimeParseException ex) {
                log.error("Invalid Date format, expected: dd-MM-yyyy", ex);
            }
        }

        return localDate;
    }
}
