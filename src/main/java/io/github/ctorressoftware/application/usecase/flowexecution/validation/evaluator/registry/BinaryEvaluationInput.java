package io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator.registry;

public record BinaryEvaluationInput(
        Object actual,
        Object expected
) implements EvaluationInput {}