package ie.gtludwig.pa.model.generic;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

public class AxisPojo extends BasePojo {

    @NotEmpty
    @Column(name = "order", nullable = false)
    private int order;

    @NotEmpty
    @Column(name = "description", nullable = false)
    private String description;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
