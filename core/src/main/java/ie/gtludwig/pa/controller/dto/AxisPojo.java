package ie.gtludwig.pa.controller.dto;

import ie.gtludwig.pa.model.Project;
import ie.gtludwig.pa.model.Rule;
import ie.gtludwig.pa.model.generic.BasePojo;

import java.util.Set;

public class AxisPojo extends BasePojo {

    private String id;

    private int ordering;

    private String description;

    private boolean applicationDefault;

    private boolean guideline;

    private int numberOfRules;

    private Set<Rule> ruleSet;

    private Project project;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOrdering() {
        return ordering;
    }

    public void setOrdering(int ordering) {
        this.ordering = ordering;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isApplicationDefault() {
        return applicationDefault;
    }

    public void setApplicationDefault(boolean applicationDefault) {
        this.applicationDefault = applicationDefault;
    }

    public boolean isGuideline() {
        return guideline;
    }

    public void setGuideline(boolean guideline) {
        this.guideline = guideline;
    }

    public int getNumberOfRules() {
        return numberOfRules;
    }

    public void setNumberOfRules(int numberOfRules) {
        this.numberOfRules = numberOfRules;
    }

    public Set<Rule> getRuleSet() {
        return ruleSet;
    }

    public void setRuleSet(Set<Rule> ruleSet) {
        this.ruleSet = ruleSet;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
