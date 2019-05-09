package ie.gtludwig.pa.service;

import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.service.generic.CrudService;

public interface UserService extends CrudService<User> {

    User findByUsername(String username);

    User findByEmail(String email);
}
