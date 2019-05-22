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
    private int ordering = 0;

    @NotEmpty
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "guideline", nullable = false)
    private boolean guideline = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinTable(name = "pa_axis2rule",
            joinColumns = @JoinColumn(name = "axisId"),
            inverseJoinColumns = @JoinColumn(name = "ruleId"))
    private Set<Rule> ruleSet;

    public Axis() {
    }

    public Axis(int ordering, @NotEmpty String description, boolean guideline, Set<Rule> ruleSet) {
        this.ordering = ordering;
        this.description = description;
        this.guideline = guideline;
        this.ruleSet = ruleSet;
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

    public boolean isGuideline() {
        return guideline;
    }

    public void setGuideline(boolean guideline) {
        this.guideline = guideline;
    }


    public Set<Rule> getRuleSet() {
        return ruleSet;
    }

    public void setRuleSet(Set<Rule> ruleSet) {
        this.ruleSet = ruleSet;
    }
}
