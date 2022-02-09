package com.usmanadio.drone.pojos;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Instant;

public class CustomLocalDateSerializer extends JsonSerializer<Instant> {

    @Override
    public void serialize(Instant localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
        throws IOException {
        String dateString = localDate.toString();
        jsonGenerator.writeString(dateString);
    }
}
