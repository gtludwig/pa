package ie.gtludwig.pa.service.impl;

import ie.gtludwig.pa.dao.UserProfileJpaRepository;
import ie.gtludwig.pa.model.UserProfile;
import ie.gtludwig.pa.service.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "userProfileService")
public class UserProfileServiceImpl implements UserProfileService {

    private static Logger logger = LoggerFactory.getLogger(UserProfileServiceImpl.class);

    @Autowired
    UserProfileJpaRepository userProfileJpaRepository;

//    @Autowired
//    UserService userService;

    @Override
    public UserProfile findById(String id) {
        return userProfileJpaRepository.getOne(id);
    }

    @Override
    public UserProfile findUserProfileByType(String type) {
        return userProfileJpaRepository.findByType(type);
    }

    @Override
    public List<UserProfile> findAllUserProfilesByUsername(String usename) {
        return userProfileJpaRepository.findAllByUsername(usename);
    }

    @Override
    public List<UserProfile> findAllUserProfilesByUserId(String ssoId) {
        return null;
    }

    @Override
    public void save(UserProfile pojo) {
        logger.info("Saved userProfile with type: " + pojo.getType());
        userProfileJpaRepository.save(pojo);
    }

    @Override
    public void remove(String id) {
        UserProfile userProfile = userProfileJpaRepository.getOne(id);
        logger.info("Removed userProfile of type: " + userProfile.getType());
        userProfileJpaRepository.delete(userProfile);
    }

    @Override
    public List<UserProfile> findAll() {
        return userProfileJpaRepository.findAll();
    }
}
