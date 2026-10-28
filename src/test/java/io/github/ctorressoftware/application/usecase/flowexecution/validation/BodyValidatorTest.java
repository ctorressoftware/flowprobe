package io.github.ctorressoftware.application.usecase.flowexecution.validation;

import io.github.ctorressoftware.application.port.out.JsonProcessor;
import io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator.registry.BinaryEvaluationInput;
import io.github.ctorressoftware.domain.model.ExpectationResult;
import io.github.ctorressoftware.domain.model.BodyExpectation;
import io.github.ctorressoftware.domain.model.ExpectationOperator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class BodyValidatorTest {

    @Mock
    private JsonProcessor jsonProcessor;

    @Mock
    private ExpectationEvaluationDispatcher dispatcher;

    private BodyValidator bodyValidator;

    @BeforeEach
    void init() {
        bodyValidator = new BodyValidator(jsonProcessor, dispatcher);
    }

    @Test
    void shouldValidateAllBodyExpectations() {

        String responseBody = """
                {
                  "name": "bulbasaur",
                  "type": "grass"
                }
                """;

        BodyExpectation nameExpectation = new BodyExpectation(
                "/name",
                ExpectationOperator.EQUALS,
                "bulbasaur"
        );

        BodyExpectation typeExpectation = new BodyExpectation(
                "/type",
                ExpectationOperator.NOT_EQUALS,
                "fire"
        );

        ExpectationResult nameResult = new ExpectationResult(
                true,
                "bulbasaur",
                "bulbasaur"
        );

        ExpectationResult typeResult = new ExpectationResult(
                true,
                "fire",
                "grass"
        );

        Mockito
                .when(jsonProcessor.extractValue(responseBody, "/name"))
                .thenReturn("bulbasaur");

        Mockito
                .when(jsonProcessor.extractValue(responseBody, "/type"))
                .thenReturn("grass");

        Mockito
                .when(dispatcher.evaluate(
                        ExpectationOperator.EQUALS,
                        new BinaryEvaluationInput("bulbasaur", "bulbasaur")
                ))
                .thenReturn(nameResult);

        Mockito
                .when(dispatcher.evaluate(
                        ExpectationOperator.NOT_EQUALS,
                        new BinaryEvaluationInput("grass", "fire")
                ))
                .thenReturn(typeResult);

        List<ExpectationResult> results = bodyValidator.validate(
                responseBody,
                List.of(nameExpectation, typeExpectation)
        );

        Assertions.assertEquals(
                List.of(nameResult, typeResult),
                results
        );

        Mockito.verify(jsonProcessor)
                .extractValue(responseBody, "/name");

        Mockito.verify(jsonProcessor)
                .extractValue(responseBody, "/type");

        Mockito.verify(dispatcher).evaluate(
                ExpectationOperator.EQUALS,
                new BinaryEvaluationInput("bulbasaur", "bulbasaur")
        );

        Mockito.verify(dispatcher).evaluate(
                ExpectationOperator.NOT_EQUALS,
                new BinaryEvaluationInput("grass", "fire")
        );

        Mockito.verifyNoMoreInteractions(
                jsonProcessor,
                dispatcher
        );
    }

    @Test
    void shouldReturnEmptyResultWhenNoBodyExpectationsAreDefined() {

        List<ExpectationResult> results = bodyValidator.validate(
                "{}",
                List.of()
        );

        Assertions.assertTrue(results.isEmpty());

        Mockito.verifyNoInteractions(
                jsonProcessor,
                dispatcher
        );
    }
}