package io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator;

import io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator.registry.ExpectationEvaluator;
import io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator.registry.UnaryEvaluationInput;
import io.github.ctorressoftware.domain.model.ExpectationOperator;
import io.github.ctorressoftware.domain.model.ExpectationResult;

public class NotExistsExpectationEvaluator
        implements ExpectationEvaluator<UnaryEvaluationInput> {

    @Override
    public ExpectationOperator operator() {
        return ExpectationOperator.NOT_EXISTS;
    }

    @Override
    public Class<UnaryEvaluationInput> inputType() {
        return UnaryEvaluationInput.class;
    }

    @Override
    public ExpectationResult evaluate(UnaryEvaluationInput input) {
        Object actual = input.actual();

        return new ExpectationResult(
                actual == null,
                true,
                actual
        );
    }
}