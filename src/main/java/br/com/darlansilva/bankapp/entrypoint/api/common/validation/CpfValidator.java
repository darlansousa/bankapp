package br.com.darlansilva.bankapp.entrypoint.api.common.validation;

import java.util.stream.IntStream;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<CPF, String> {
    @Override
    public void initialize(CPF constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext constraintValidatorContext) {
        try {
            if (cpf == null) {
                return false;
            }

            final var cpfNumbersOnly = cpf.replaceAll("\\D", "");
            int sum = IntStream.range(0, 9)
                    .map(i -> (cpfNumbersOnly.charAt(i) - '0') * (10 - i))
                    .sum();
            int digit = 11 - (sum % 11);
            digit = (digit > 9) ? 0 : digit;

            int secondSum = IntStream.range(0, 10)
                    .map(i -> (cpfNumbersOnly.charAt(i) - '0') * (11 - i))
                    .sum();
            int secondDigit = 11 - (secondSum % 11);
            secondDigit = (secondDigit > 9) ? 0 : secondDigit;

            return (cpfNumbersOnly.charAt(9) - '0') == digit &&
                    (cpfNumbersOnly.charAt(10) - '0') == secondDigit;
        } catch (Exception e) {
            return false;
        }
    }
}
