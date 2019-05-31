package ie.gtludwig.pa.controller.dto;

import ie.gtludwig.pa.model.Axis;
import ie.gtludwig.pa.model.Project;

public class RulePojo {

    private String id;

    private int ordering;

    private String description;

    private Axis axisOrGuideline;

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

    public Axis getAxisOrGuideline() {
        return axisOrGuideline;
    }

    public void setAxisOrGuideline(Axis axisOrGuideline) {
        this.axisOrGuideline = axisOrGuideline;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
