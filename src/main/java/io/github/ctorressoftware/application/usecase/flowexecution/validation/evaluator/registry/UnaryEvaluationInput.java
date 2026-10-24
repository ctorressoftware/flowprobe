package io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator.registry;

public record UnaryEvaluationInput(
        Object actual
) implements EvaluationInput {}