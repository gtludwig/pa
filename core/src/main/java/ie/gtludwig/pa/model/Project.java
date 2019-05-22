package ie.gtludwig.pa.model;

import ie.gtludwig.pa.model.generic.BasePojo;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "pa_project")
public class Project  extends BasePojo {

    private static final Long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "creator")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "sponsor")
    private User sponsor;

    @Column(name = "creationDate", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @Column(name = "evaluationStart", nullable = false)
    private LocalDateTime evaluationStart;

    @Column(name = "evaluationEnd", nullable = false)
    private LocalDateTime evaluationEnd;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "counter")
    private int counter = 0;

    @Column(name = "ideal")
    private int ideal;

    @Column(name = "state", nullable = false)
    private ProjectState state = ProjectState.DRAFT;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "guidelineAxis")
    private Axis guidelineAxis;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    @JoinTable(name = "pa_project2axis",
            joinColumns = @JoinColumn(name = "projectId"),
            inverseJoinColumns = @JoinColumn(name = "axisId"))
    private Set<Axis> axisSet;

//    @OneToMany(mappedBy = "project")
//    private Set<Analysis> analysisSet;


    public Project() {
    }

    public Project(User creator, User sponsor, LocalDateTime creationDate, LocalDateTime evaluationStart, LocalDateTime evaluationEnd, String name, String description, int counter, int ideal, ProjectState state, Axis guidelineAxis) {
        this.creator = creator;
        this.sponsor = sponsor;
        this.creationDate = creationDate;
        this.evaluationStart = evaluationStart;
        this.evaluationEnd = evaluationEnd;
        this.name = name;
        this.description = description;
        this.counter = counter;
        this.ideal = ideal;
        this.state = state;
        this.guidelineAxis = guidelineAxis;
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
//
//    public Set<Analysis> getAnalysisSet() {
//        return analysisSet;
//    }
//
//    public void setAnalysisSet(Set<Analysis> analysisSet) {
//        this.analysisSet = analysisSet;
//    }
}
