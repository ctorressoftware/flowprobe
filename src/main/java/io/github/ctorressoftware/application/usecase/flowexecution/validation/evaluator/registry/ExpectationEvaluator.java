package io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator.registry;

import io.github.ctorressoftware.domain.model.ExpectationOperator;
import io.github.ctorressoftware.domain.model.ExpectationResult;

public interface ExpectationEvaluator<T extends EvaluationInput> {
    ExpectationOperator operator();
    Class<T> inputType();
    ExpectationResult evaluate(T input);
}