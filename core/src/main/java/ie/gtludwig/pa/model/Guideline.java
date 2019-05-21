package ie.gtludwig.pa.model;

import ie.gtludwig.pa.model.generic.BasePojo;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Entity
@Table(name = "pa_guideline")
public class Guideline extends BasePojo {

    private static final Long serialVersionUID = 1L;

    @Column(name = "ordering", nullable = false)
    private int ordering = 0;

    @NotEmpty
    @Column(name = "description", nullable = false)
    private String description;

    @NotEmpty
    @Column(name = "name", nullable = false)
    private String name;

//    @NotEmpty
//    @OneToOne(mappedBy = "guideline")
//    private Project project;

    @Transient
    private int numberOfRules = 1;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinTable(name = "pa_guideline2rule",
            joinColumns = @JoinColumn(name = "guidelineId"),
            inverseJoinColumns = @JoinColumn(name = "ruleId"))
    private Set<Rule> rulesSet;

    public Guideline() {
    }

    public Guideline(int ordering, @NotEmpty String description, @NotEmpty String name) {
        this.ordering = ordering;
        this.description = description;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
//
//    public Project getProject() {
//        return project;
//    }
//
//    public void setProject(Project project) {
//        this.project = project;
//    }

    public int getNumberOfRules() {
        return numberOfRules;
    }

    public void setNumberOfRules(int numberOfRules) {
        this.numberOfRules = numberOfRules;
    }

    public Set<Rule> getRulesSet() {
        return rulesSet;
    }

    public void setRulesSet(Set<Rule> ruleSet) {
        this.rulesSet = rulesSet;
    }
}
