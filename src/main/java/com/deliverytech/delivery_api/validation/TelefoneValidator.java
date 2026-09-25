package com.deliverytech.delivery_api.validation;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TelefoneValidator implements ConstraintValidator<ValidTelefone, String> {

    private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    @Override
    public void initialize(ValidTelefone constraintAnnotation) {
        // Inicialização se necessário
    }

    @Override
    public boolean isValid(String telefone, ConstraintValidatorContext context) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return false;
        }

        try {
            // O segundo argumento ("BR") define o país padrão caso o número
            // venha sem o DDI explícito (ex: digitação nacional com DDD).
            // Se o usuário mandar com o DDI (ex: +55...), a biblioteca identifica
            // automaticamente.
            Phonenumber.PhoneNumber numeroParsed = phoneUtil.parse(telefone, "BR");

            // Valida se o número é estruturalmente válido para o país
            return phoneUtil.isValidNumber(numeroParsed);

        } catch (NumberParseException e) {
            // Caso o formato esteja totalmente inválido ou ilegível
            return false;
        }
    }
}