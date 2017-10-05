package org.verapdf.crawler.api.validation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.verapdf.crawler.api.document.DomainDocument;
import org.verapdf.crawler.api.validation.error.ValidationError;

import java.util.*;

public class VeraPDFValidationResult {
    private boolean isValid = false;
    private List<ValidationError> validationErrors = new ArrayList<>();
    private Map<String, String> properties = new HashMap<>();

    public VeraPDFValidationResult() {
    }

    public VeraPDFValidationResult(String validationErrorMessage) {
        this.validationErrors.add(new ValidationError(validationErrorMessage));
    }

    @JsonProperty
    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }

    @JsonProperty
    public void setValidationErrors(List<ValidationError> validationErrors) {
        this.validationErrors = validationErrors == null ? Collections.emptyList() : new ArrayList<>(validationErrors);
    }

    public void addValidationError(ValidationError error) {
        this.validationErrors.add(error);
    }

    @JsonProperty
    public boolean isValid() {
        return isValid;
    }

    @JsonProperty
    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

    @JsonProperty
    public Map<String, String> getProperties() {
        return properties;
    }

    @JsonProperty
    public void setProperties(Map<String, String> properties) {
        this.properties = properties == null ? Collections.emptyMap() : new HashMap<>(properties);
    }

    public void addProperty(String name, String value) {
        properties.put(name, value);
    }

    @JsonIgnore
    public DomainDocument.BaseTestResult getBaseTestResult() {
        return this.isValid() ? DomainDocument.BaseTestResult.OPEN : DomainDocument.BaseTestResult.NOT_OPEN;
    }
}
