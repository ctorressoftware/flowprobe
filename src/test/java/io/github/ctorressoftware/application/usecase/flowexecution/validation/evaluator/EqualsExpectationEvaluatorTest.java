package io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator;

import io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator.registry.BinaryEvaluationInput;
import io.github.ctorressoftware.application.usecase.flowexecution.validation.extraction.ExtractedValue;
import io.github.ctorressoftware.domain.model.ExpectationResult;
import io.github.ctorressoftware.domain.model.ExpectationOperator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EqualsExpectationEvaluatorTest {

    private EqualsExpectationEvaluator evaluator;

    @BeforeEach
    void init() {
        evaluator = new EqualsExpectationEvaluator();
    }

    @Test
    void shouldSupportEqualsOperator() {

        Assertions.assertEquals(
                ExpectationOperator.EQUALS,
                evaluator.operator()
        );
    }

    @Test
    void shouldReturnSuccessfulResultWhenValuesAreEqual() {

        ExpectationResult result = evaluator.evaluate(new BinaryEvaluationInput(
                new ExtractedValue("bulbasaur", true),
                "bulbasaur"
        ));

        Assertions.assertTrue(result.successful());
        Assertions.assertEquals("bulbasaur", result.expected());
        Assertions.assertEquals("bulbasaur", result.actual());
    }

    @Test
    void shouldReturnFailedResultWhenValuesAreDifferent() {

        ExpectationResult result = evaluator.evaluate(new BinaryEvaluationInput(
                new ExtractedValue("pikachu", true),
                "bulbasaur"
        ));

        Assertions.assertFalse(result.successful());
        Assertions.assertEquals("bulbasaur", result.expected());
        Assertions.assertEquals("pikachu", result.actual());
    }

    @Test
    void shouldReturnFailedResultWhenActualPathIsMissing() {

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
    void shouldReturnSuccessfulResultWhenPresentValueAndExpectedAreNull() {

        ExpectationResult result = evaluator.evaluate(
                new BinaryEvaluationInput(
                        new ExtractedValue(null, true),
                        null
                )
        );

        Assertions.assertTrue(result.successful());
        Assertions.assertNull(result.expected());
        Assertions.assertNull(result.actual());
    }

    @Test
    void shouldReturnFailedResultWhenOnlyActualValueIsNull() {

        ExpectationResult result = evaluator.evaluate(new BinaryEvaluationInput(
                new ExtractedValue(null, false),
                "bulbasaur"
        ));

        Assertions.assertFalse(result.successful());
        Assertions.assertEquals("bulbasaur", result.expected());
        Assertions.assertNull(result.actual());
    }

    @Test
    void shouldReturnFailedResultWhenOnlyExpectedValueIsNull() {

        ExpectationResult result = evaluator.evaluate(new BinaryEvaluationInput(
                new ExtractedValue("bulbasaur", true),
                null
        ));

        Assertions.assertFalse(result.successful());
        Assertions.assertNull(result.expected());
        Assertions.assertEquals("bulbasaur", result.actual());
    }
}