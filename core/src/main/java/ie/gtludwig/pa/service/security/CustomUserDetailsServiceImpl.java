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

import java.util.HashSet;
import java.util.Set;


@Service(value = "customUserDetailsService")
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(CustomUserDetailsServiceImpl.class);

    @Autowired
    private UserService userService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userService.findByEmail(email);
        if (user == null) {
            logger.error("User with email" + email + " not found.");
            throw new UsernameNotFoundException("Oops! User not found with email: " + email);
        } else {

            boolean enabled = true;
            boolean accountNonExpired = true;
            boolean credentialsNonExpired = true;
            boolean accountNonLocked = true;
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            for (UserProfile userProfile : user.getUserProfileSet()) {
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + userProfile.getType()));
            }
            logger.info("User with email {} successfully logged", user.getEmail());
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), enabled, accountNonExpired,
                    credentialsNonExpired, accountNonLocked, grantedAuthorities);
        }
    }
}