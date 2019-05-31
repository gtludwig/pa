package ie.gtludwig.pa.service.impl;


import ie.gtludwig.pa.dao.UserJpaRepository;
import ie.gtludwig.pa.model.*;
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
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User findByUsername(String username) {
        return userJpaRepository.findByUsername(username);
    }

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
    public void createUser(String username, String email, String password, String firstName, String lastName, Set<UserProfile> userProfileSet) throws EmailExistsException {
        if (userJpaRepository.findByEmail(email) == null) {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPassword(password);
            user.setState(UserState.INACTIVE.getState());
            user.setUserProfileSet(userProfileSet);

            save(user);
        } else {
            throw new EmailExistsException("There is an account with that email address: " + email);
        }
    }

    @Override
    public void createInvitedUser(String username, String email, String password, String firstName, String lastName, Set<UserProfile> userProfileSet) throws EmailExistsException {
        if (userJpaRepository.findByEmail(email) == null) {
            User user = new User();
            user.setUsername(username);
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
    public void updateUser(String userId, String username, String email, String password, String firstName, String lastName, Set<UserProfile> userProfileSet) {
        User user = findById(userId);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUserProfileSet(userProfileSet);

        save(user);
    }

    @Override
    public VerificationToken getVerificationToken(String token) {
        return verificationTokenService.findByToken(token);
    }

    @Override
    public void createVerificationTokenForUser(User user, String token) {
        verificationTokenService.createVerificationTokenForUser(user, token);
    }

    @Override
    public VerificationToken generateNewVerificationToken(String existingVerificationToken) {
        return verificationTokenService.generateNewVerificationToken(existingVerificationToken);
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
        logger.info("Saved user with username: " + pojo.getUsername());
        pojo.setPassword(bCryptPasswordEncoder.encode(pojo.getPassword()));
        userJpaRepository.save(pojo);
    }

    @Override
    public void remove(String id) {
        User user = userJpaRepository.getOne(id);
        final VerificationToken verificationToken = verificationTokenService.findByUser(user);

        if (verificationToken != null) {
            verificationTokenService.delete(verificationToken);
        }

        logger.info("Removed user with username: " + user.getUsername());
        userJpaRepository.delete(user);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll();
    }
}
