package io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator;

import io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator.registry.BinaryEvaluationInput;
import io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator.registry.ExpectationEvaluator;
import io.github.ctorressoftware.application.usecase.flowexecution.validation.extraction.ExtractedValue;
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
        ExtractedValue actual = input.actual();
        Object expected = input.expected();

        return new ExpectationResult(
                actual.found() && Objects.equals(actual.value(), expected),
                expected,
                actual.value()
        );
    }
}
