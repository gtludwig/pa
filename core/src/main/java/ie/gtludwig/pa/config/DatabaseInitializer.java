package ie.gtludwig.pa.config;

import ie.gtludwig.pa.model.*;
import ie.gtludwig.pa.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

@Component
@ConditionalOnProperty(name = "app.db-init", havingValue = "true")
public class DatabaseInitializer {


    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private GuidelineService guidelineService;

    @Autowired
    private AxisService axisService;


    @Autowired
    private RuleService ruleService;

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);
    
    private Set<UserProfile> userProfileSet;

    private User master;
    
    private Project project;
    private static final String project_name = "Default project";
    private static final String project_description = "Default description";


    private Guideline guideline;
    private Set<Rule> guidelineRuleSet;
    private static final String guideline_description = "Default Guideline description";
    private static final String guideline_name = "Default Guideline name";
    private static final String guidelineRule0_description = "Default Guideline Rule 0";
    private static final String guidelineRule1_description = "Default Guideline Rule 1";
    private static final String guidelineRule2_description = "Default Guideline Rule 2";
    private Rule guidelineRule0;
    private Rule guidelineRule1;
    private Rule guidelineRule2;

    private Set<Axis> axisSet;
    private Axis axis0;
    private Axis axis1;
    private Axis axis2;
    private Set<Rule> axis0RuleSet;
    private Set<Rule> axis1RuleSet;
    private Set<Rule> axis2RuleSet;
    private static final String axis0_description = "Default Axis 0";
    private static final String axis1_description = "Default Axis 1";
    private static final String axis2_description = "Default Axis 2";
    private static final String axisRule00_description = "Default Axis 0 Rule 0";
    private static final String axisRule01_description = "Default Axis 0 Rule 1";
    private static final String axisRule02_description = "Default Axis 0 Rule 2";
    private static final String axisRule10_description = "Default Axis 1 Rule 0";
    private static final String axisRule11_description = "Default Axis 1 Rule 1";
    private static final String axisRule12_description = "Default Axis 1 Rule 2";
    private static final String axisRule20_description = "Default Axis 2 Rule 0";
    private static final String axisRule21_description = "Default Axis 2 Rule 1";
    private static final String axisRule22_description = "Default Axis 2 Rule 2";


    @PostConstruct
    public void run() {
        createUserProfiles();

        createUsers();

        createDefaultProject();

//        createDefaultGuideline();

        createDefaultAxisSet();
    }

    protected void createUserProfiles() {

        UserProfile user = userProfileService.findUserProfileByType(UserProfileType.USER.getUserProfileType());
        UserProfile specialist = userProfileService.findUserProfileByType(UserProfileType.SPECIALIST.getUserProfileType());
        UserProfile sponsor = userProfileService.findUserProfileByType(UserProfileType.SPONSOR.getUserProfileType());
        UserProfile admin = userProfileService.findUserProfileByType(UserProfileType.ADMIN.getUserProfileType());

        logger.info("initializing creation of default user profiles");

        if (user == null) {
            logger.info("creating user profile USER");
            user = new UserProfile(UserProfileType.USER.getUserProfileType());
            userProfileService.save(user);
            logger.info("user profile of type <<{}>> created", user.getType());
        } else {
            logger.info("user profile fo type <<{}>> ALREADY created", user.getType());
        }

        if (specialist == null) {
            logger.info("creating user profile SPECIALIST");
            specialist = new UserProfile(UserProfileType.SPECIALIST.getUserProfileType());
            userProfileService.save(specialist);
            logger.info("user profile of type <<{}>> created", specialist.getType());
        } else {
            logger.info("user profile fo type <<{}>> ALREADY created", specialist.getType());
        }


        if (sponsor == null) {
            logger.info("creating user profile SPONSOR");
            sponsor = new UserProfile(UserProfileType.SPONSOR.getUserProfileType());
            userProfileService.save(sponsor);
            logger.info("user profile of type <<{}>> created", sponsor.getType());
        } else {
            logger.info("user profile fo type <<{}>> ALREADY created", sponsor.getType());
        }

        if (admin == null) {
            logger.info("creating user profile ADMIN");
            admin = new UserProfile(UserProfileType.ADMIN.getUserProfileType());
            userProfileService.save(admin);
            logger.info("user profile of type <<{}>> created", admin.getType());
        } else {
            logger.info("user profile fo type <<{}>> ALREADY created", admin.getType());
        }
        userProfileSet = new HashSet<>();

        userProfileSet.add(user);
        userProfileSet.add(specialist);
        userProfileSet.add(sponsor);
        userProfileSet.add(admin);
    }

    protected void createUsers() {

        logger.info("creating user MASTER");

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

            logger.info("user <<{}>> ALREADY CREATED", master.getUsername());
        }
    }

    protected void createDefaultProject() {
        if(projectService.findByName(project_name) == null) {

            axisSet = new HashSet<>();
//            guideline = new Guideline();

            project = new Project();
            project.setCreator(master);
            project.setSponsor(master);
            project.setCreationDate(LocalDateTime.now());
            project.setEvaluationStart(project.getCreationDate());
            project.setEvaluationEnd(project.getCreationDate().plus(10, ChronoUnit.DAYS));
            project.setName(project_name);
            project.setDescription(project_description);
            project.setCounter(0);
            project.setIdeal(5);
            project.setState(ProjectState.DRAFT);
            project.setGuideline(createDefaultGuideline());
            project.setAxisSet(axisSet);

            try {
                projectService.save(project);
            } catch (Exception e) {
                logger.error("project not saved");
            }
        } else {
            project = projectService.findByName(project_name);
        }
    }

    protected Guideline createDefaultGuideline() {

        guidelineRuleSet = createGuidelineRuleSet();

        logger.info("creating default Guideline");
        if (guidelineService.findByDescription(guideline_description) == null) {
            guideline = new Guideline(0, guideline_description, guideline_name);
            try {
                guidelineService.save(guideline);
                logger.info("Guideline with description <<{}>> successfully saved.", guideline.getDescription());
            } catch (Exception e) {
                logger.error("Error creating Guideline with description <<{}>>.", guideline_description);
            }
        } else {
            guideline = guidelineService.findByDescription(guideline_description);
        }

        return guideline;
    }

    protected Set<Rule> createGuidelineRuleSet() {
        guidelineRuleSet = new HashSet<>();

        logger.info("creating default Guideline Rule set");
        if (ruleService.findByDescription(guidelineRule0_description) == null) {
            guidelineRule0 = new Rule(0, guidelineRule0_description);
            try {
                ruleService.save(guidelineRule0);
                logger.info("Guideline rule with description <<{}>> successfully saved.", guidelineRule0.getDescription());
            } catch (Exception e) {
                logger.error("Error creating Guideline default rule 0");
            }
        } else {
            guidelineRule0 = ruleService.findByDescription(guidelineRule0_description);
        }

        if (ruleService.findByDescription(guidelineRule1_description) == null) {
            guidelineRule1 = new Rule(1, guidelineRule1_description);
            try {
                ruleService.save(guidelineRule1);
                logger.info("Guideline rule with description <<{}>> successfully saved.", guidelineRule1.getDescription());
            } catch (Exception e) {
                 logger.error("Error creating Guideline default rule 1");
            }
        } else {
            guidelineRule1 = ruleService.findByDescription(guidelineRule1_description);
        }

        if (ruleService.findByDescription(guidelineRule2_description) == null) {
            guidelineRule2 = new Rule(2, guidelineRule2_description);
            try {
                ruleService.save(guidelineRule2);
                logger.info("Guideline rule with description <<{}>> successfully saved.", guidelineRule2.getDescription());
            } catch (Exception e) {
                logger.error("Error creating Guideline default rule 2");
            }
        } else {
            guidelineRule2 = ruleService.findByDescription(guidelineRule2_description);
        }

        guidelineRuleSet.add(guidelineRule0);
        guidelineRuleSet.add(guidelineRule1);
        guidelineRuleSet.add(guidelineRule2);

        return guidelineRuleSet;
    }

    protected void createDefaultAxisSet() {

        axisSet = new HashSet<>();
        axisSet.add(createAxis0());
        axisSet.add(createAxis1());
        axisSet.add(createAxis2());

    }

    protected Axis createAxis0() {

        if (axisService.findByDescription(axis0_description) == null) {
            axis0 = new Axis();
            axis0.setOrdering(0);
            axis0.setDescription(axis0_description);
            axis0.setActive(true);
            axis0.setProject(project);
            axis0.setRuleSet(createAxis0RuleSet());

            try {
                axisService.save(axis0);
                logger.info("Axis with description <<{}>> successfully saved.", axis0_description);
            } catch (Exception e) {
                logger.error("Error creating axis with description <<{}>>.", axis0_description);
            }
        } else {
            axis0 = axisService.findByDescription(axis0_description);
        }

        return axis0;
    }

    protected Set<Rule> createAxis0RuleSet() {
        axis0RuleSet = new HashSet<>();

        axis0RuleSet.add(new Rule(0, axisRule00_description));
        axis0RuleSet.add(new Rule(1, axisRule01_description));
        axis0RuleSet.add(new Rule(2, axisRule02_description));

        return axis0RuleSet;
    }

    protected Axis createAxis1() {


        if (axisService.findByDescription(axis1_description) == null) {
            axis1 = new Axis();
            axis1.setOrdering(1);
            axis1.setDescription(axis1_description);
            axis1.setActive(true);
            axis1.setProject(project);
            axis1.setRuleSet(createAxis1RuleSet());

            try {
                axisService.save(axis1);
                logger.info("Axis with description <<{}>> successfully saved.", axis1_description);
            } catch (Exception e) {
                logger.error("Error creating axis with description <<{}>>.", axis1_description);
            }
        } else {
            axis1 = axisService.findByDescription(axis1_description);
        }

        return axis1;
    }

    protected Set<Rule> createAxis1RuleSet() {
        axis1RuleSet = new HashSet<>();

        axis1RuleSet.add(new Rule(0, axisRule10_description));
        axis1RuleSet.add(new Rule(1, axisRule11_description));
        axis1RuleSet.add(new Rule(2, axisRule12_description));

        return axis1RuleSet;
    }

    protected Axis createAxis2() {

        if (axisService.findByDescription(axis2_description) == null) {
            axis2 = new Axis();
            axis2.setOrdering(2);
            axis2.setDescription(axis2_description);
            axis2.setActive(true);
            axis2.setProject(project);
            axis2.setRuleSet(createAxis2RuleSet());

            try {
                axisService.save(axis2);
                logger.info("Axis with description <<{}>> successfully saved.", axis2_description);
            } catch (Exception e) {
                logger.error("Error creating axis with description <<{}>>.", axis2_description);
            }
        } else {
            axis2 = axisService.findByDescription(axis2_description);
        }

        return axis2;
    }
    protected Set<Rule> createAxis2RuleSet() {
        axis2RuleSet = new HashSet<>();

        axis2RuleSet.add(new Rule(0, axisRule20_description));
        axis2RuleSet.add(new Rule(1, axisRule21_description));
        axis2RuleSet.add(new Rule(2, axisRule22_description));

        return axis2RuleSet;
    }
}
