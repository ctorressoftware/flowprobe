package io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator;

import io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator.registry.UnaryEvaluationInput;
import io.github.ctorressoftware.domain.model.ExpectationResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExistsExpectationEvaluatorTest {

    private ExistsExpectationEvaluator evaluator;

    @BeforeEach
    void init() {
        evaluator = new ExistsExpectationEvaluator();
    }

    @Test
    void shouldReturnTrueIfValueExistsInJsonObject() {

        UnaryEvaluationInput input = new UnaryEvaluationInput(230);

        ExpectationResult result = evaluator.evaluate(input);

        Assertions.assertTrue(result.successful());
        Assertions.assertNotNull(result.actual());
    }

    @Test
    void shouldReturnFalseIfValueNotExistsInJsonObject() {

        UnaryEvaluationInput input = new UnaryEvaluationInput(null);

        ExpectationResult result = evaluator.evaluate(input);

        Assertions.assertFalse(result.successful());
        Assertions.assertNull(result.actual());
    }
}
