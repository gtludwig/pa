package ie.gtludwig.pa.model;

import ie.gtludwig.pa.model.generic.BasePojo;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
@Table(name = "pa_analysis")
public class Analysis extends BasePojo {

    private static final long serialVersionUID = 1L;

    @NotEmpty
    @Column(name = "analyst", nullable = false)
    private User analyst;

    @NotEmpty
    @Column(name = "evaluationDate")
    private Date evaluationDate;

    @NotEmpty
    @Column(name = "latest", nullable = false)
    private boolean latest = true;

    @NotEmpty
    @Column(name = "result", nullable = false)
    private float result = 0f;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

    public User getAnalyst() {
        return analyst;
    }

    public void setAnalyst(User analyst) {
        this.analyst = analyst;
    }

    public Date getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(Date evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public boolean isLatest() {
        return latest;
    }

    public void setLatest(boolean latest) {
        this.latest = latest;
    }

    public float getResult() {
        return result;
    }

    public void setResult(float result) {
        this.result = result;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
