package org.unitedinternet.cosmo.hibernate.validator;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class ValidationConfig {

    @Autowired
    private SummaryValidationConfig summaryConfig;

    @Autowired
    private LocationValidationConfig locationConfig;

    @Autowired
    private DescriptionValidationConfig descriptionConfig;

    @Autowired
    private AttendeesValidationConfig attendeesConfig;

    @Autowired
    private RecurrenceValidationConfig recurrenceConfig;

    @Autowired
    private IcalDataValidationConfig icaldataConfig;

    /**
     * Default constructor.
     */
    public ValidationConfig() {

    }

    @PostConstruct
    public void initEventValidator() {
        EventValidator.setValidationConfig(this);
    }

    public SummaryValidationConfig getSummaryConfig() {
        return summaryConfig;
    }

    public LocationValidationConfig getLocationConfig() {
        return locationConfig;
    }

    public DescriptionValidationConfig getDescriptionConfig() {
        return descriptionConfig;
    }

    public AttendeesValidationConfig getAttendeesConfig() {
        return attendeesConfig;
    }

    public RecurrenceValidationConfig getRecurrenceConfig() {
        return recurrenceConfig;
    }

    public IcalDataValidationConfig getIcaldataConfig() {
        return icaldataConfig;
    }

    public int getIcaldataMaxLength() {
        return 0;
    }

    public Collection<Object> getAllowedRecurrenceFrequencies() {
        return null;
    }

    public int getAttendeesMaxSize() {
        return 0;
    }

    public int getDescriptionMinLength() {
        return 0;
    }

    public int getDescriptionMaxLength() {
        return 0;
    }

    public int getLocationMinLength() {
        return 0;
    }

    public int getLocationMaxLength() {
        return 0;
    }

    public int getSummaryMinLength() {
        return 0;
    }

    public int getSummaryMaxLength() {
        return 0;
    }
}
