package ie.gtludwig.pa.model;

import ie.gtludwig.pa.model.generic.BasePojo;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "pa_project")
public class Project  extends BasePojo {

    private static final Long serialVersionUID = 1L;

    @NotEmpty
    @Column(name = "creator", nullable = false)
    private User creator;

    @NotEmpty
    @Column(name = "creationDate", nullable = false)
    private Date creationDate;

    @NotEmpty
    @Column(name = "evaluationStart", nullable = false)
    private Date evaluationStart;

    @NotEmpty
    @Column(name = "evaluationEnd", nullable = false)
    private Date evaluationEnd;

    @NotEmpty
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "counter")
    private int counter;

    @Column(name = "ideal")
    private int ideal;

    @Column(name = "status", nullable = false)
    private ProjectState state = ProjectState.DRAFT;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "guidelineId", referencedColumnName = "id")
    private Guideline guideline;

    @OneToMany(mappedBy = "project")
    private Set<Axis> axisSet;

    @OneToMany(mappedBy = "project")
    private Set<Analysis> analysisSet;

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getEvaluationStart() {
        return evaluationStart;
    }

    public void setEvaluationStart(Date evaluationStart) {
        this.evaluationStart = evaluationStart;
    }

    public Date getEvaluationEnd() {
        return evaluationEnd;
    }

    public void setEvaluationEnd(Date evaluationEnd) {
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

    public Guideline getGuideline() {
        return guideline;
    }

    public void setGuideline(Guideline guideline) {
        this.guideline = guideline;
    }

    public Set<Axis> getAxisSet() {
        return axisSet;
    }

    public void setAxisSet(Set<Axis> axisSet) {
        this.axisSet = axisSet;
    }

    public Set<Analysis> getAnalysisSet() {
        return analysisSet;
    }

    public void setAnalysisSet(Set<Analysis> analysisSet) {
        this.analysisSet = analysisSet;
    }
}
