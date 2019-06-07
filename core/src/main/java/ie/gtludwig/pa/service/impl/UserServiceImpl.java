package ie.gtludwig.pa.service.impl;


import ie.gtludwig.pa.dao.UserJpaRepository;
import ie.gtludwig.pa.model.*;
import ie.gtludwig.pa.service.PasswordResetTokenService;
import ie.gtludwig.pa.service.UserProfileService;
import ie.gtludwig.pa.service.UserService;
import ie.gtludwig.pa.service.VerificationTokenService;
import ie.gtludwig.pa.validation.EmailExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User findByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }

    @Override
    public List<UserProfile> findAllUserProfiles() {
        return userProfileService.findAll();
    }

    @Override
    public List<UserProfile> findAllUserProfiles(String loggedUsername) {
        User loggedUser = findByEmail(getPrincipal());
        UserProfile userProfile = userProfileService.findUserProfileByType(UserProfileType.ADMIN.getUserProfileType());

        if (loggedUser.getUserProfileSet().contains(userProfile)) {
            return userProfileService.findAll();
        } else {
            return loggedUser.getUserProfileSet().stream().collect(Collectors.toList());
        }
    }

    private String getPrincipal() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public List<User> findAllByUserProfileType(UserProfileType userProfileType) {
        List<User> userProfileTypeList = new ArrayList<>();
        for (User user : userJpaRepository.findAll()) {
            for (UserProfile userProfile : user.getUserProfileSet()) {
                if (userProfile.getType().equalsIgnoreCase(userProfileType.getUserProfileType())) {
                    userProfileTypeList.add(user);
                }
            }
        }
        return userProfileTypeList;
    }

    @Override
    public Set<UserProfile> getSelfRegistrationUserProfileSet() {
        return userProfileService.getSelfRegistrationUserProfileSet();
    }

    @Override
    public Set<UserProfile> getInvitedSpecialistUserProfileSet() {
        return getInvitedSpecialistUserProfileSet();
    }

    @Override
    public void createInvitedUser(String email, String password, String firstName, String lastName, Set<UserProfile> userProfileSet) throws EmailExistsException {
        if (userJpaRepository.findByEmail(email) == null) {
            User user = new User();
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPassword(password);
            user.setState(UserState.INVITED.getState());
            user.setUserProfileSet(userProfileSet);

            save(user);
        } else {
            throw new EmailExistsException("There is an account with that email address: " + email);
        }
    }

    @Override
    public User newUserRegistration(String email, String firstName, String lastName, String password) throws EmailExistsException {
        if (userJpaRepository.findByEmail(email) == null) {
            User user = new User();
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPassword(bCryptPasswordEncoder.encode(password));
            user.setState(UserState.INVITED.getState());
            user.setUserProfileSet(userProfileService.getSelfRegistrationUserProfileSet());

            return userJpaRepository.saveAndFlush(user);

        } else {
            throw new EmailExistsException("There is already an account with this email address: " + email);
        }
    }

    @Override
    public void createUser(String email, String password, String firstName, String lastName, Set<UserProfile> userProfileSet) throws EmailExistsException {
        if (userJpaRepository.findByEmail(email) == null) {
            User user = new User();
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPassword(password);
            user.setState(UserState.INACTIVE.getState());
            user.setUserProfileSet(userProfileSet);

            save(user);
        } else {
            throw new EmailExistsException("There is already an account with this email address: " + email);
        }
    }

    @Override
    public void updateUser(String userId, String email, String firstName, String lastName, Set<UserProfile> userProfileSet) {
        User user = findById(userId);
        if (user != null) {
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUserProfileSet(userProfileSet);

            try {
                save(user);
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
            }
        }
    }

    @Override
    public void updateUser(String userId, String email, String password, String firstName, String lastName, Set<UserProfile> userProfileSet) {
        User user = findById(userId);
        user.setEmail(email);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUserProfileSet(userProfileSet);

        save(user);
    }

    @Override
    public void updatePasswordForUser(String email, String password) {
        User user = findByEmail(email);
        updateUser(user.getId(), user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getUserProfileSet());
        if (passwordResetTokenService.findByUser(user) != null) {
            deletePasswordResetTokenByUser(user);
        }
    }

    @Override
    public VerificationToken getVerificationToken(String token) {
        return verificationTokenService.findByToken(token);
    }

    @Override
    public PasswordResetToken getPasswordResetToken(String passwordResetToken) {
        return passwordResetTokenService.findByToken(passwordResetToken);
    }

    @Override
    public void createVerificationTokenForUser(User user, String token) {
        verificationTokenService.createVerificationTokenForUser(user, token);
    }

    @Override
    public void createPasswordResetToken(User user, String token) {
        passwordResetTokenService.createPasswordResetTokenForUser(user, token);
    }

    @Override
    public VerificationToken generateNewVerificationToken(String existingVerificationToken) {
        return verificationTokenService.generateNewVerificationToken(existingVerificationToken);
    }

    @Override
    public void deleteVerificationTokenByUser(User user) {
        verificationTokenService.deleteVerificationTokenByUser(user);
    }

    @Override
    public void deletePasswordResetTokenByUser(User user) {
        passwordResetTokenService.deletePasswordResetTokenByUser(user);
    }

    @Override
    public User getUser(String verificationToken) {
        return verificationTokenService.findByToken(verificationToken).getUser();
    }

    @Override
    public void saveRegisteredUser(User user) {
        save(user);
    }

    @Override
    public User findById(String id) {
        return userJpaRepository.getOne(id);
    }

    @Override
    public void save(User pojo) {
        logger.info("Saved user with email: " + pojo.getEmail());
        pojo.setPassword(bCryptPasswordEncoder.encode(pojo.getPassword()));
        userJpaRepository.save(pojo);
    }

    @Override
    public void remove(String id) {
        User user = userJpaRepository.getOne(id);
        final VerificationToken verificationToken = verificationTokenService.findByUser(user);
        final PasswordResetToken passwordResetToken = passwordResetTokenService.findByUser(user);

        if (verificationToken != null) {
            verificationTokenService.delete(verificationToken);
        }

        if (passwordResetToken != null) {
            passwordResetTokenService.delete(passwordResetToken);
        }

        logger.info("Removed user with email: " + user.getEmail());
        userJpaRepository.delete(user);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll();
    }
}
