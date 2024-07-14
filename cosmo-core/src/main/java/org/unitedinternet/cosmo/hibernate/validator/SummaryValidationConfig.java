package org.unitedinternet.cosmo.hibernate.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SummaryValidationConfig {

    @Value("${cosmo.event.validation.summary.min.length}")
    private int summaryMinLength;

    @Value("${cosmo.event.validation.summary.max.length}")
    private int summaryMaxLength;

    public int getSummaryMinLength() {
        return summaryMinLength;
    }

    public int getSummaryMaxLength() {
        return summaryMaxLength;
    }
}
