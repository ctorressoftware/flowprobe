package io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator;

import io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator.registry.BinaryEvaluationInput;
import io.github.ctorressoftware.application.usecase.flowexecution.validation.extraction.ExtractedValue;
import io.github.ctorressoftware.domain.model.ExpectationOperator;
import io.github.ctorressoftware.domain.model.ExpectationResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContainsExpectationEvaluatorTest {

    private ContainsExpectationEvaluator evaluator;

    @BeforeEach
    void init() {
        evaluator = new ContainsExpectationEvaluator();
    }

    @Test
    void shouldSupportContainsOperator() {

        Assertions.assertEquals(
                ExpectationOperator.CONTAINS,
                evaluator.operator()
        );
    }

    @Test
    void shouldReturnSuccessfulResultWhenSentenceContainsSubString() {

        ExpectationResult result = evaluator.evaluate(new BinaryEvaluationInput(
                new ExtractedValue("mega-bulbasaur", true),
                "bulbasaur"
        ));

        Assertions.assertTrue(result.successful());
    }

    @Test
    void shouldReturnFailedResultWhenSentenceNotContainsSubString() {

        ExpectationResult result = evaluator.evaluate(new BinaryEvaluationInput(
                new ExtractedValue("pikachu", true),
                "bulbasaur"
        ));

        Assertions.assertFalse(result.successful());
        Assertions.assertEquals("bulbasaur", result.expected());
        Assertions.assertEquals("pikachu", result.actual());
    }
}
