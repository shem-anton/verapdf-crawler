package org.verapdf.crawler.api.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.verapdf.crawler.db.document.InsertDocumentDao;

import java.util.List;
import java.util.Map;

public class DomainDocument {
    private String url;
    private String contentType;
    private InsertDocumentDao.TestResultSummary testResultSummary;
    private List<String> errors;
    private Map<String, String> properties;

    public DomainDocument() {
    }

    @JsonProperty
    public String getUrl() {
        return url;
    }

    @JsonProperty
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty
    public String getContentType() {
        return contentType;
    }

    @JsonProperty
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @JsonProperty
    public InsertDocumentDao.TestResultSummary getStatus() {
        return testResultSummary;
    }

    @JsonProperty
    public void setStatus(InsertDocumentDao.TestResultSummary testResultSummary) {
        this.testResultSummary = testResultSummary;
    }

    @JsonProperty
    public List<String> getErrors() {
        return errors;
    }

    @JsonProperty
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    @JsonProperty
    public Map<String, String> getProperties() {
        return properties;
    }

    @JsonProperty
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
