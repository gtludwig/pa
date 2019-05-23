package ie.gtludwig.pa.controller.dto;

import ie.gtludwig.pa.model.Axis;
import ie.gtludwig.pa.model.ProjectState;
import ie.gtludwig.pa.model.User;

import java.time.LocalDateTime;
import java.util.Set;

public class ProjectPojo {

    private String id;

    private User creator;

    private User sponsor;

    private LocalDateTime creationDate;

    private LocalDateTime evaluationStart;

    private LocalDateTime evaluationEnd;

    private String name;

    private String description;

    private int counter;

    private int ideal;

    private ProjectState state = ProjectState.DRAFT;

    private Axis guidelineAxis;

    private Set<Axis> axisSet;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getSponsor() {
        return sponsor;
    }

    public void setSponsor(User sponsor) {
        this.sponsor = sponsor;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getEvaluationStart() {
        return evaluationStart;
    }

    public void setEvaluationStart(LocalDateTime evaluationStart) {
        this.evaluationStart = evaluationStart;
    }

    public LocalDateTime getEvaluationEnd() {
        return evaluationEnd;
    }

    public void setEvaluationEnd(LocalDateTime evaluationEnd) {
        this.evaluationEnd = evaluationEnd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getIdeal() {
        return ideal;
    }

    public void setIdeal(int ideal) {
        this.ideal = ideal;
    }

    public ProjectState getState() {
        return state;
    }

    public void setState(ProjectState state) {
        this.state = state;
    }

    public Axis getGuidelineAxis() {
        return guidelineAxis;
    }

    public void setGuidelineAxis(Axis guidelineAxis) {
        this.guidelineAxis = guidelineAxis;
    }

    public Set<Axis> getAxisSet() {
        return axisSet;
    }

    public void setAxisSet(Set<Axis> axisSet) {
        this.axisSet = axisSet;
    }
}
