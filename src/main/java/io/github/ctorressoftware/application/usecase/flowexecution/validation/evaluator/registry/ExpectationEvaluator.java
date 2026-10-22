package io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator;

import io.github.ctorressoftware.domain.model.ExpectationOperator;

public interface ExpectationEvaluator {
    ExpectationOperator operator();
}
