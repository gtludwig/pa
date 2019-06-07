package ie.gtludwig.pa.service;

import ie.gtludwig.pa.model.*;
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

    void updatePasswordForUser(String email, String password);

    VerificationToken getVerificationToken(String verificationToken);

    PasswordResetToken getPasswordResetToken(String passwordResetToken);

    void createVerificationTokenForUser(User user, String token);

    void createPasswordResetToken(User user, String token);

    void deleteVerificationTokenByUser(User user);

    void deletePasswordResetTokenByUser(User user);

    VerificationToken generateNewVerificationToken(final String existingVerificationToken);

    User getUser(String verificationToken);

    void saveRegisteredUser(User user);
}
