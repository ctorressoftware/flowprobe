package io.github.ctorressoftware.application.port.out;

import io.github.ctorressoftware.application.usecase.flowexecution.validation.extraction.ExtractedValue;

public interface JsonProcessor {
    String serialize(Object value);
    <T> T deserialize(String serializedJson, Class<T> dataType);
    ExtractedValue extractValue(String json, String path);
}
