package io.github.ctorressoftware.application.usecase.flowexecution.validation;

import io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator.registry.EvaluationInput;
import io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator.registry.ExpectationEvaluator;
import io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator.registry.ExpectationEvaluatorRegistry;
import io.github.ctorressoftware.domain.model.ExpectationOperator;
import io.github.ctorressoftware.domain.model.ExpectationResult;

public final class ExpectationEvaluationDispatcher {

    private final ExpectationEvaluatorRegistry registry;

    public ExpectationEvaluationDispatcher(ExpectationEvaluatorRegistry registry) {
        this.registry = registry;
    }

    public ExpectationResult evaluate(ExpectationOperator operator, EvaluationInput input) {
        return execute(registry.get(operator), input);
    }

    private <T extends EvaluationInput> ExpectationResult execute(
            ExpectationEvaluator<T> evaluator,
            EvaluationInput input
    ) {
        return evaluator.evaluate(evaluator.inputType().cast(input));
    }
}