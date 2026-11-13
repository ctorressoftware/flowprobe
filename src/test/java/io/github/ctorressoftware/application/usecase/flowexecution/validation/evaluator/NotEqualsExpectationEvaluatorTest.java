package io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator;

import io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator.registry.BinaryEvaluationInput;
import io.github.ctorressoftware.application.usecase.flowexecution.validation.extraction.ExtractedValue;
import io.github.ctorressoftware.domain.model.ExpectationResult;
import io.github.ctorressoftware.domain.model.ExpectationOperator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotEqualsExpectationEvaluatorTest {

    private NotEqualsExpectationEvaluator evaluator;

    @BeforeEach
    void init() {
        evaluator = new NotEqualsExpectationEvaluator();
    }

    @Test
    void shouldSupportNotEqualsOperator() {
        Assertions.assertEquals(ExpectationOperator.NOT_EQUALS, evaluator.operator());
    }

    @Test
    void shouldReturnSuccessfulResultWhenValuesAreDifferent() {

        ExpectationResult result = evaluator.evaluate(new BinaryEvaluationInput(
                new ExtractedValue("pikachu", true),
                "bulbasaur"
        ));

        Assertions.assertTrue(result.successful());
        Assertions.assertEquals("bulbasaur", result.expected());
        Assertions.assertEquals("pikachu", result.actual());
    }

    @Test
    void shouldReturnFailedResultWhenValuesAreEqual() {

        ExpectationResult result = evaluator.evaluate(new BinaryEvaluationInput(
                new ExtractedValue("bulbasaur", true),
                "bulbasaur"
        ));

        Assertions.assertFalse(result.successful());
        Assertions.assertEquals("bulbasaur", result.expected());
        Assertions.assertEquals("bulbasaur", result.actual());
    }

    @Test
    void shouldReturnFailedResultWhenBothValuesAreNull() {

        ExpectationResult result = evaluator.evaluate(
                new BinaryEvaluationInput(
                        new ExtractedValue(null, false),
                        null
                )
        );

        Assertions.assertFalse(result.successful());
        Assertions.assertNull(result.expected());
        Assertions.assertNull(result.actual());
    }

    @Test
    void shouldReturnFailedResultWhenActualPathIsMissing() {

        ExpectationResult result = evaluator.evaluate(
                new BinaryEvaluationInput(
                        new ExtractedValue(null, false),
                        "bulbasaur"
                )
        );

        Assertions.assertFalse(result.successful());
        Assertions.assertEquals("bulbasaur", result.expected());
        Assertions.assertNull(result.actual());
    }

    @Test
    void shouldReturnSuccessfulResultWhenPresentActualValueIsNull() {

        ExpectationResult result = evaluator.evaluate(
                new BinaryEvaluationInput(
                        new ExtractedValue(null, true),
                        "bulbasaur"
                )
        );

        Assertions.assertTrue(result.successful());
        Assertions.assertEquals("bulbasaur", result.expected());
        Assertions.assertNull(result.actual());
    }

    @Test
    void shouldReturnSuccessfulResultWhenOnlyExpectedValueIsNull() {

        ExpectationResult result = evaluator.evaluate(
                new BinaryEvaluationInput(
                        new ExtractedValue("bulbasaur", true),
                        null
                )
        );

        Assertions.assertTrue(result.successful());
        Assertions.assertNull(result.expected());
        Assertions.assertEquals("bulbasaur", result.actual());
    }
}