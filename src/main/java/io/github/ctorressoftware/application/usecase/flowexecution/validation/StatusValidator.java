package io.github.ctorressoftware.application.usecase.flowexecution.validation;

import io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator.registry.BinaryEvaluationInput;
import io.github.ctorressoftware.domain.model.ExpectationResult;
import io.github.ctorressoftware.domain.model.ExpectationOperator;

public final class StatusValidator {

    private final ExpectationEvaluationDispatcher dispatcher;

    public StatusValidator(ExpectationEvaluationDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public ExpectationResult validate(
            int actualStatus,
            Integer expectedStatus) {

        return dispatcher.evaluate(
                ExpectationOperator.EQUALS,
                new BinaryEvaluationInput(actualStatus, expectedStatus)
        );
    }
}