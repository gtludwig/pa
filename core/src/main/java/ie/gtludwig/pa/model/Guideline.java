package ie.gtludwig.pa.model;

import ie.gtludwig.pa.model.generic.AxisPojo;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Entity
@Table(name = "pa_guideline")
public class Guideline extends AxisPojo {

    private static final Long serialVersionUID = 1L;

    @NotEmpty
    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(mappedBy = "project")
    private Project project;

    @OneToMany(mappedBy = "guidelineElement")
    private Set<GuidelineElement> guidelineElementSet;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Set<GuidelineElement> getGuidelineElementSet() {
        return guidelineElementSet;
    }

    public void setGuidelineElementSet(Set<GuidelineElement> guidelineElementSet) {
        this.guidelineElementSet = guidelineElementSet;
    }
}
