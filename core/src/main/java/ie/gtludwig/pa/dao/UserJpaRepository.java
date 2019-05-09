package ie.gtludwig.pa.dao;

import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "userJpaRepository")
public interface UserJpaRepository extends JpaRepository<User, String> {

	User findByUsername(String username);

	User findByEmail(String email);

	List<User> findByUserProfile(UserProfile userProfile);

	void deleteById(String id);
}
