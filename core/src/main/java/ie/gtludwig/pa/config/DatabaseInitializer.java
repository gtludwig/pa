package ie.gtludwig.pa.config;

import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.model.UserProfile;
import ie.gtludwig.pa.model.UserProfileType;
import ie.gtludwig.pa.model.UserState;
import ie.gtludwig.pa.service.UserProfileService;
import ie.gtludwig.pa.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
@ConditionalOnProperty(name = "app.db-init", havingValue = "true")
public class DatabaseInitializer {


    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    private Set<UserProfile> userProfileSet;

    @PostConstruct
    public void run() {
        createUserProfiles();

        createUsers();
    }

    protected void createUserProfiles() {

        UserProfile user = userProfileService.findUserProfileByType(UserProfileType.USER.getUserProfileType());
        UserProfile admin = userProfileService.findUserProfileByType(UserProfileType.ADMIN.getUserProfileType());
        UserProfile specialist = userProfileService.findUserProfileByType(UserProfileType.SPECIALIST.getUserProfileType());

        logger.info("initializing creation of default user profiles");

        if (user == null) {
            logger.info("creating user profile USER");
            user = new UserProfile(UserProfileType.USER.getUserProfileType());
            userProfileService.save(user);
            logger.info("user profile of type <<{}>> created", user.getType());
        } else {
            logger.info("user profile fo type <<{}>> ALREADY created", user.getType());
        }

        if (admin == null) {
            logger.info("creating user profile ADMIN");
            admin = new UserProfile(UserProfileType.ADMIN.getUserProfileType());
            userProfileService.save(admin);
            logger.info("user profile of type <<{}>> created", admin.getType());
        } else {
            logger.info("user profile fo type <<{}>> ALREADY created", admin.getType());
        }

        if (specialist == null) {
            logger.info("creating user profile SPECIALIST");
            specialist = new UserProfile(UserProfileType.SPECIALIST.getUserProfileType());
            userProfileService.save(specialist);
            logger.info("user profile of type <<{}>> created", specialist.getType());
        } else {
            logger.info("user profile fo type <<{}>> ALREADY created", specialist.getType());
        }

        userProfileSet = new HashSet<>();

        userProfileSet.add(user);
        userProfileSet.add(admin);
        userProfileSet.add(specialist);
    }

    protected void createUsers() {

        logger.info("creating user MASTER");
        User master;

        if (userService.findByUsername("master") == null) {
            master = new User();

            master.setUsername("master");
            master.setFirstName("MasterUser");
            master.setLastName("DefaultUser");
            master.setEmail("gustavo.ludwig@gmail.com");
            master.setPassword("gus1234");
            master.setState(UserState.ACTIVE.getState());
            master.setUserProfileSet(userProfileSet);

            try {
                userService.save(master);
            } catch (Exception e) {
                logger.error("user not saved");
            }
        } else {
            master = userService.findByUsername("master");

            logger.info("user << {} >> ALREADY CREATED", master.getUsername());
        }



    }
}
