package io.github.ctorressoftware.application.usecase.flowexecution.validation;

import io.github.ctorressoftware.application.usecase.flowexecution.validation.evaluator.registry.BinaryEvaluationInput;
import io.github.ctorressoftware.domain.model.ExpectationResult;
import io.github.ctorressoftware.domain.constant.HttpStatusCode;
import io.github.ctorressoftware.domain.model.ExpectationOperator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StatusValidatorTest {

    @Mock
    private ExpectationEvaluationDispatcher dispatcher;
    private StatusValidator statusValidator;

    @BeforeEach
    void init() {
        statusValidator = new StatusValidator(dispatcher);
    }

    @Test
    void shouldValidateStatusUsingEqualsEvaluator() {

        int actualStatus = HttpStatusCode.OK;
        Integer expectedStatus = HttpStatusCode.OK;

        ExpectationResult expectedResult = new ExpectationResult(
                true,
                expectedStatus,
                actualStatus
        );

        BinaryEvaluationInput input = new BinaryEvaluationInput(
                actualStatus,
                expectedStatus
        );

        Mockito
                .when(dispatcher.evaluate(ExpectationOperator.EQUALS, input))
                .thenReturn(expectedResult);

        ExpectationResult result = statusValidator.validate(
                actualStatus,
                expectedStatus
        );

        Assertions.assertSame(expectedResult, result);

        Mockito.verify(dispatcher).evaluate(
                ExpectationOperator.EQUALS,
                input
        );

        Mockito.verifyNoMoreInteractions(dispatcher);
    }
}