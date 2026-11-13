package io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator.registry;

import io.github.ctorressoftware.application.usecase.flowexecution.validation.extraction.ExtractedValue;

public record UnaryEvaluationInput(
        ExtractedValue actual
) implements EvaluationInput {}