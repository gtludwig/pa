package ie.gtludwig.pa.service.security;

import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.model.UserProfile;
import ie.gtludwig.pa.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service(value = "loginDetailsService")
@Transactional(readOnly = true)
public class LoginDetailsService implements UserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(LoginDetailsService.class);

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User " + username + " not found.");
            throw new UsernameNotFoundException("Oops! User not found with username: " + username);
        } else {
            logger.info("User " + username + " successfully logged");
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthorities(user));
        }
    }

    private Collection<GrantedAuthority> getAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (UserProfile userProfile : user.getUserProfileSet()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userProfile.getType()));
        }

        return authorities;
    }
}
