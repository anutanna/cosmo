package org.unitedinternet.cosmo.hibernate.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AttendeesValidationConfig {

    @Value("${cosmo.event.validation.attendees.max.length}")
    private int attendeesMaxSize;

    public int getAttendeesMaxSize() {
        return attendeesMaxSize;
    }
}
