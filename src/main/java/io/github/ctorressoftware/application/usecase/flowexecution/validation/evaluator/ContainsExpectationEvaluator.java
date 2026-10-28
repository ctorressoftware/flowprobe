package io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator;

import io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator.registry.BinaryEvaluationInput;
import io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator.registry.ExpectationEvaluator;
import io.github.ctorressoftware.domain.model.ExpectationOperator;
import io.github.ctorressoftware.domain.model.ExpectationResult;

public class ContainsExpectationEvaluator
        implements ExpectationEvaluator<BinaryEvaluationInput> {

    @Override
    public ExpectationOperator operator() {
        return ExpectationOperator.CONTAINS;
    }

    @Override
    public Class<BinaryEvaluationInput> inputType() {
        return BinaryEvaluationInput.class;
    }

    @Override
    public ExpectationResult evaluate(BinaryEvaluationInput input) {

        boolean successful =
                input.expected() instanceof String sentenceStr
                        && input.actual() instanceof String subStringStr
                        && subStringStr.contains(sentenceStr);

        return new ExpectationResult(
                successful,
                input.expected(),
                input.actual()
        );
    }
}
