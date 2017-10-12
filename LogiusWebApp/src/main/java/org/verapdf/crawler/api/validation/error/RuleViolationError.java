package org.verapdf.crawler.api.validation.error;

import javax.persistence.*;

@Entity
@DiscriminatorValue("RULE_VIOLATION")
public class RuleViolationError extends ValidationError {

    @Embedded
    private Rule rule;

    public RuleViolationError() {
    }

    public RuleViolationError(String specification, String clause, String testNumber, String description) {
        super(description);
        this.rule = new Rule(specification, clause, testNumber);
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }
}
