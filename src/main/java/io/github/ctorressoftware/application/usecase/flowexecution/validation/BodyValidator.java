package io.github.ctorressoftware.application.usecase.flowexecution.validation;

import io.github.ctorressoftware.application.port.out.JsonProcessor;
import io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator.registry.*;
import io.github.ctorressoftware.application.usecase.flowexecution.validation.extraction.ExtractedValue;
import io.github.ctorressoftware.domain.model.ExpectationOperator;
import io.github.ctorressoftware.domain.model.ExpectationResult;
import io.github.ctorressoftware.domain.model.BodyExpectation;

import java.util.List;

public final class BodyValidator {
    private final JsonProcessor jsonProcessor;
    private final ExpectationEvaluationDispatcher dispatcher;

    public BodyValidator(
            JsonProcessor jsonProcessor,
            ExpectationEvaluationDispatcher dispatcher
    ) {
        this.jsonProcessor = jsonProcessor;
        this.dispatcher = dispatcher;
    }

    public List<ExpectationResult> validate(String responseBody, List<BodyExpectation> expectations) {
        return expectations.stream()
                .map(expectation -> validateExpectation(responseBody, expectation))
                .toList();
    }

    private ExpectationResult validateExpectation(
            String responseBody,
            BodyExpectation expectation
    ) {
        ExpectationOperator operator = expectation.operator();

        ExtractedValue extracted = jsonProcessor.extractValue(
                responseBody,
                expectation.path()
        );

        EvaluationInput input = operator.requiresValue()
                ? new BinaryEvaluationInput(extracted, expectation.expectedValue())
                : new UnaryEvaluationInput(extracted);

        return dispatcher.evaluate(operator, input);
    }
}
