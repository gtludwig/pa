package ie.gtludwig.pa.dao;

import ie.gtludwig.pa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, String> {

//	User findByUsername(String username);

	User findByEmail(String email);

	void deleteById(String id);
}
