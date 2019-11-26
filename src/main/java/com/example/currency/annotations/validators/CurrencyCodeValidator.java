package com.example.currency.annotations.validators;

import com.example.currency.Constants;
import com.example.currency.annotations.CurrencyCodeConstraint;
import com.example.currency.api.CacheManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

@Component
public class CurrencyCodeValidator implements ConstraintValidator<CurrencyCodeConstraint, Object> {

    private  static final Logger logger = LoggerFactory.getLogger(CurrencyCodeValidator.class);

    @Autowired
    CacheManagerService cacheManagerService;

    @Override
    public void initialize(CurrencyCodeConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object param, ConstraintValidatorContext context) {

        if (param != null && param instanceof List) {
            logger.debug("Invoking list codes validator");
            return validate((List<String>) param);
        }

        if (param != null && param instanceof  String) {
            logger.debug("Invoking String code(s) validator");
            return validate((String) param);
        }

        return false;
    }

    private boolean validate (List<String> codesToValidate) {
        logger.debug("validate - Validating list of currency codes: " + codesToValidate);
        List<String> validCodes = (List<String>) getDataFromCache(Constants.CURRENT_CACHE.getKey(), Constants.VALID_CODES.getKey());
        long invalidCodeCount = 0;
        invalidCodeCount = codesToValidate.stream().filter(code -> !validCodes.contains(code.toUpperCase())).count();
        return (invalidCodeCount == 0);
    }

    private boolean validate (String codeToValidate) {
        logger.debug("validate - Validating String of currency code(s): " + codeToValidate);
        List<String> validCodes = (List<String>) getDataFromCache(Constants.CURRENT_CACHE.getKey(), Constants.VALID_CODES.getKey());
        long invalidCodeCount = 0;

        if (codeToValidate.contains(",")) {
            List<String> codes = Arrays.asList(codeToValidate.split("\\s*,\\s*"));
            invalidCodeCount = codes.stream().filter(code -> !validCodes.contains(code.toUpperCase())).count();
        } else if (!validCodes.contains(codeToValidate.toUpperCase())) {
            invalidCodeCount++;
        }

        return (invalidCodeCount == 0);
    }

    private Object getDataFromCache (String cacheName, String key) {

        Object cacheData = cacheManagerService.getCacheForKey(
                Constants.CURRENT_CACHE.getKey(), Constants.VALID_CODES.getKey());

        return cacheData;

    }
}
