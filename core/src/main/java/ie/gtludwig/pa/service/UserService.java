package ie.gtludwig.pa.service;

import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.model.UserProfile;
import ie.gtludwig.pa.model.UserProfileType;
import ie.gtludwig.pa.model.VerificationToken;
import ie.gtludwig.pa.service.generic.CrudService;
import ie.gtludwig.pa.validation.EmailExistsException;

import java.util.List;
import java.util.Set;

public interface UserService extends CrudService<User> {

//    User findByUsername(String username);

    User findByEmail(String email);

    List<UserProfile> findAllUserProfiles();

    List<UserProfile> findAllUserProfiles(String loggedUser);

    Set<UserProfile> getSelfRegistrationUserProfileSet();

    Set<UserProfile> getInvitedSpecialistUserProfileSet();

    // TODO - refactor this
    List<User> findAllByUserProfileType(UserProfileType userProfileType);

    User newUserRegistration(String email, String firstName, String lastName, String password) throws EmailExistsException;

    void createUser(String email, String password, String firstName, String lastName, Set<UserProfile> userProfileSet) throws EmailExistsException;

    void createInvitedUser(String email, String password, String firstName, String lastName, Set<UserProfile> userProfileSet) throws EmailExistsException;

    void updateUser(String userId, String email, String firstName, String lastName, Set<UserProfile> userProfileSet);

    void updateUser(String userId, String email, String password, String firstName, String lastName, Set<UserProfile> userProfileSet);

    VerificationToken getVerificationToken(String verificationToken);

    void createVerificationTokenForUser(User user, String token);

    VerificationToken generateNewVerificationToken(final String existingVerificationToken);

    User getUser(String verificationToken);

    void saveRegisteredUser(User user);
}
