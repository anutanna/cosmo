package org.unitedinternet.cosmo.hibernate.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class RecurrenceValidationConfig {

    @Value("${cosmo.event.validation.allowed.recurrence.frequencies}")
    private String[] allowedRecurrenceFrequencies;

    public List<String> getAllowedRecurrenceFrequencies() {
        return Arrays.asList(allowedRecurrenceFrequencies);
    }
}
