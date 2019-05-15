package ie.gtludwig.pa.service;

import ie.gtludwig.pa.model.Axis;
import ie.gtludwig.pa.model.AxisElement;
import ie.gtludwig.pa.service.generic.CrudService;

import java.util.List;

public interface AxisElementService extends CrudService<AxisElement> {

    List<AxisElement> findAllByAxis(Axis axis);

    void createDefaulAxisElementSetFromAxis(Axis axis) ;

}
