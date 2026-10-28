package io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator;

import io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator.registry.BinaryEvaluationInput;
import io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator.registry.ExpectationEvaluator;
import io.github.ctorressoftware.domain.model.ExpectationResult;
import io.github.ctorressoftware.domain.model.ExpectationOperator;

import java.util.Objects;

public class EqualsExpectationEvaluator
        implements ExpectationEvaluator<BinaryEvaluationInput> {

    @Override
    public ExpectationOperator operator() {
        return ExpectationOperator.EQUALS;
    }

    @Override
    public Class<BinaryEvaluationInput> inputType() {
        return BinaryEvaluationInput.class;
    }

    @Override
    public ExpectationResult evaluate(BinaryEvaluationInput input) {
        Object actual = input.actual();
        Object expected = input.expected();

        return new ExpectationResult(
                Objects.equals(actual, expected),
                expected,
                actual
        );
    }
}
