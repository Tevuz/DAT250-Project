package no.hvl.dat250.g13.project.domain.Identifiers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotNull;

import java.io.IOException;
import java.io.Serializable;

@JsonSerialize(using = SurveyKey.Serializer.class)
public record SurveyKey(@NotNull Long value) implements Serializable {

    static class Serializer extends JsonSerializer<SurveyKey> {
        @Override
        public void serialize(SurveyKey key, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeNumber(key.value());
        }
    }
}
