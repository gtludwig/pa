package ie.gtludwig.pa.model;

import ie.gtludwig.pa.model.generic.BasePojo;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Entity
@Table(name = "pa_axis")
public class Axis extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Column(name = "ordering", nullable = false)
    private int ordering;

    @NotEmpty
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "active", nullable = false)
    private boolean active = true;

//    @NotEmpty
    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinTable(name = "pa_axis2rule",
            joinColumns = @JoinColumn(name = "axisId"),
            inverseJoinColumns = @JoinColumn(name = "ruleId"))
    private Set<Rule> ruleSet;


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

    public Set<Rule> getRuleSet() {
        return ruleSet;
    }

    public void setRuleSet(Set<Rule> ruleSet) {
        this.ruleSet = ruleSet;
    }
}
