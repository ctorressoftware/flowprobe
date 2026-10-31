package io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator;

import io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator.registry.UnaryEvaluationInput;
import io.github.ctorressoftware.domain.model.ExpectationResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotExistsExpectationEvaluatorTest {

    private NotExistsExpectationEvaluator evaluator;

    @BeforeEach
    void init() {
        evaluator = new NotExistsExpectationEvaluator();
    }

    @Test
    void shouldReturnTrueIfValueNotExistsInJsonObject() {
        UnaryEvaluationInput input = new UnaryEvaluationInput(null);

        ExpectationResult result = evaluator.evaluate(input);

        Assertions.assertTrue(result.successful());
        Assertions.assertNull(result.actual());
    }

    @Test
    void shouldReturnFalseIfValueExistsInJsonObject() {
        UnaryEvaluationInput input = new UnaryEvaluationInput(Object.class);

        ExpectationResult result = evaluator.evaluate(input);

        Assertions.assertFalse(result.successful());
        Assertions.assertNotNull(result.actual());
    }
}
