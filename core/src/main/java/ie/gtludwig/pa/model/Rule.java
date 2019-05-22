package ie.gtludwig.pa.model;

import ie.gtludwig.pa.model.generic.BasePojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "pa_rule")
public class Rule extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Column(name = "ordering", nullable = false)
    private int ordering;

    @NotEmpty
    @Column(name = "description", nullable = false)
    private String description;

    public Rule() {}

    public Rule(@NotEmpty int ordering, @NotEmpty String description) {
        this.ordering = ordering;
        this.description = description;
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
}

