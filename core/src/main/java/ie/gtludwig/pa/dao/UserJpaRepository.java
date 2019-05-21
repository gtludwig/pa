package ie.gtludwig.pa.dao;

import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.model.UserProfile;
import ie.gtludwig.pa.model.UserProfileType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserJpaRepository extends JpaRepository<User, String> {

	User findByUsername(String username);

	User findByEmail(String email);

	void deleteById(String id);
}
