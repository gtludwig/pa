package ie.gtludwig.pa.dao;

import ie.gtludwig.pa.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileJpaRepository extends JpaRepository<UserProfile, String> {

    UserProfile findByType(String type);

//    List<UserProfile> findAllByUsername(String username);
}
