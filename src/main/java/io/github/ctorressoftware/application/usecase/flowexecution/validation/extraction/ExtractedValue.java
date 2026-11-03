package io.github.ctorressoftware.application.usecase.flowexecution.validation.extraction;

public record ExtractedValue(
        Object value,
        boolean found
) {}