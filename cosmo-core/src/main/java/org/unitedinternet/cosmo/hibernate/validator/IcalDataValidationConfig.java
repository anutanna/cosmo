package org.unitedinternet.cosmo.hibernate.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IcalDataValidationConfig {

    @Value("${cosmo.event.validation.icaldata.max.length}")
    private int icaldataMaxLength;

    public int getIcaldataMaxLength() {
        return icaldataMaxLength;
    }
}
