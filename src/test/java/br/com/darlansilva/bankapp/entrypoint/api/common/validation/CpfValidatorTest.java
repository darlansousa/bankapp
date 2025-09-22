package br.com.darlansilva.bankapp.entrypoint.api.common.validation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CpfValidatorTest {

    private final CpfValidator validator = new CpfValidator();



    @Test
    void shouldValidateCPFToValidNumbers() {
        assertTrue(validator.isValid("707.005.730-03", null));
        assertTrue(validator.isValid("11144477735", null));
        assertTrue(validator.isValid("93541134780", null));
    }

    @Test
    void shouldReturnFalseToInvalidCPF() {
        assertFalse(validator.isValid("12345658909", null));
        assertFalse(validator.isValid("11144457735", null));
        assertFalse(validator.isValid("93541134760", null));
    }

}