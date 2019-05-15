package ie.gtludwig.pa.model;

import ie.gtludwig.pa.model.generic.AxisPojo;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "pa_element")
public class GuidelineElement extends AxisPojo {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "guidelineId")
    private Guideline guideline;

    public Guideline getGuideline() {
        return guideline;
    }

    public void setGuideline(Guideline guideline) {
        this.guideline = guideline;
    }
}
