package ie.gtludwig.pa.model;

import ie.gtludwig.pa.model.generic.AxisPojo;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Entity
@Table(name = "pa_axis")
public class Axis extends AxisPojo {

    private static final long serialVersionUID = 1L;

    @NotEmpty
    @Column(name = "active", nullable = false)
    private boolean active = true;

    @NotEmpty
    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

    @OneToMany(mappedBy = "axis")
    private Set<AxisElement> axisElementSet;



    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Set<AxisElement> getAxisElementSet() {
        return axisElementSet;
    }

    public void setAxisElementSet(Set<AxisElement> axisElementSet) {
        this.axisElementSet = axisElementSet;
    }
}
