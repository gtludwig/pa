package ie.gtludwig.pa.service;

import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.model.UserProfile;
import ie.gtludwig.pa.model.UserProfileType;
import ie.gtludwig.pa.service.generic.CrudService;

import java.util.List;

public interface UserService extends CrudService<User> {

    User findByUsername(String username);

    User findByEmail(String email);

    List<UserProfile> findAllUserProfiles();

    // TODO - refactor this
    List<User> findAllByUserProfileType(UserProfileType userProfileType);

}
