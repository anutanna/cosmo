package org.unitedinternet.cosmo.hibernate.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DescriptionValidationConfig {

    @Value("${cosmo.event.validation.description.min.length}")
    private int descriptionMinLength;

    @Value("${cosmo.event.validation.description.max.length}")
    private int descriptionMaxLength;

    public int getDescriptionMinLength() {
        return descriptionMinLength;
    }

    public int getDescriptionMaxLength() {
        return descriptionMaxLength;
    }
}
