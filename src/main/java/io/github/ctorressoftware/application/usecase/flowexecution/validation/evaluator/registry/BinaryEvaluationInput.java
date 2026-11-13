package io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator.registry;

import io.github.ctorressoftware.application.usecase.flowexecution.validation.extraction.ExtractedValue;

public record BinaryEvaluationInput(
        ExtractedValue actual,
        Object expected
) implements EvaluationInput {}