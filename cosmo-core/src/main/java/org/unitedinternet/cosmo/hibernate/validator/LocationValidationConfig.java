package org.unitedinternet.cosmo.hibernate.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LocationValidationConfig {

    @Value("${cosmo.event.validation.location.min.length}")
    private int locationMinLength;

    @Value("${cosmo.event.validation.location.max.length}")
    private int locationMaxLength;

    public int getLocationMinLength() {
        return locationMinLength;
    }

    public int getLocationMaxLength() {
        return locationMaxLength;
    }
}
