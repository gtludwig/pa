package ie.gtludwig.pa.model;

import ie.gtludwig.pa.model.generic.AxisPojo;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "pa_axisElement")
public class AxisElement extends AxisPojo {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "axisId")
    private Axis axis;

    public Axis getAxis() {
        return axis;
    }

    public void setAxis(Axis axis) {
        this.axis = axis;
    }
}
