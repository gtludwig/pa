package ie.gtludwig.pa.service;

import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.model.UserProfile;
import ie.gtludwig.pa.model.UserProfileType;
import ie.gtludwig.pa.service.generic.CrudService;

import java.util.List;
import java.util.Set;

public interface UserService extends CrudService<User> {

    User findByUsername(String username);

    User findByEmail(String email);

    List<UserProfile> findAllUserProfiles();

    List<UserProfile> findAllUserProfiles(String loggedUser);

    // TODO - refactor this
    List<User> findAllByUserProfileType(UserProfileType userProfileType);

    void createUser(String username, String email, String password, String firstName, String lastName, Set<UserProfile> userProfileSet);

    void updateUser(String userId, String username, String email, String password, String firstName, String lastName, Set<UserProfile> userProfileSet);

}
