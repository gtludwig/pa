package ie.gtludwig.pa.service.generic;

import ie.gtludwig.pa.model.generic.BasePojo;

import java.util.List;

public interface CrudService<Pojo extends BasePojo> extends Service {

    Pojo findById(final String id);

    void save(Pojo pojo);

    void remove(String id);

    List<Pojo> findAll();
}
