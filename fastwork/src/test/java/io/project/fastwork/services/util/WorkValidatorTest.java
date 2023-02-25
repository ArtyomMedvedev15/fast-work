package io.project.fastwork.services.util;

import io.project.fastwork.domains.Work;
import io.project.fastwork.services.exception.TypeWorkInvalidParameterException;
import io.project.fastwork.services.exception.WorkInvalidDataValues;
import io.project.fastwork.util.UserArgumentProviders;
import io.project.fastwork.util.WorkArgumentProviders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static org.junit.jupiter.api.Assertions.*;

class WorkValidatorTest {

    @ParameterizedTest
    @ArgumentsSource(WorkArgumentProviders.WorkArgumentValidProviders.class)
    void WorkValidDataValuesTest_WithValidValue_ReturnTrue(Work work_valid) throws WorkInvalidDataValues {
        assertTrue(WorkValidator.WorkValidDataValues(work_valid));
    }
    @ParameterizedTest
    @ArgumentsSource(WorkArgumentProviders.WorkArgumentInvalidProviders.class)
    void WorkValidDataValuesTest_WithInvalidValue_ReturnTrue(Work work_invalid) throws WorkInvalidDataValues {
        WorkInvalidDataValues workInvalidDataValues = assertThrows(
                WorkInvalidDataValues.class,
                () -> WorkValidator.WorkValidDataValues(work_invalid)
        );
        assertTrue(workInvalidDataValues.getMessage().contentEquals("Invalid data values for work, check string parameters"));

    }
}