package io.project.fastwork.services.util;

import io.project.fastwork.domains.Work;
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
    void WorkValidDataValuesTest_WithValidValud_ReturnTrue(Work work_valid) throws WorkInvalidDataValues {
        assertTrue(WorkValidator.WorkValidDataValues(work_valid));

    }
}