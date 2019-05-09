package ie.gtludwig.pa.service.impl;

import ie.gtludwig.pa.dao.UserJpaRepository;
import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserJpaRepository userJpaRepository;



    @Override
    public User findByUsername(String username) {
        return userJpaRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }

    @Override
    public User findById(String id) {
        return userJpaRepository.getOne(id);
    }

    @Override
    public void save(User pojo) {
        logger.info("Saved user with username: " + pojo.getUsername());

    }

    @Override
    public void remove(String id) {
        User user = userJpaRepository.getOne(id);
        logger.info("Removed user with username: " + user.getUsername());
        userJpaRepository.delete(user);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll();
    }
}
