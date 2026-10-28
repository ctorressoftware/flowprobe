package io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator;

import io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator.registry.BinaryEvaluationInput;
import io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator.registry.ExpectationEvaluator;
import io.github.ctorressoftware.domain.model.ExpectationResult;
import io.github.ctorressoftware.domain.model.ExpectationOperator;

import java.util.Objects;

public class NotEqualsExpectationEvaluator
        implements ExpectationEvaluator<BinaryEvaluationInput> {

    @Override
    public ExpectationOperator operator() {
        return ExpectationOperator.NOT_EQUALS;
    }

    @Override
    public Class<BinaryEvaluationInput> inputType() {
        return BinaryEvaluationInput.class;
    }

    @Override
    public ExpectationResult evaluate(BinaryEvaluationInput input) {

        boolean successful = !Objects.equals(input.actual(), input.expected());

        return new ExpectationResult(
                successful,
                input.expected(),
                input.actual()
        );
    }
}
