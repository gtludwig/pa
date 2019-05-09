package ie.gtludwig.pa.dao;

import ie.gtludwig.pa.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "userProfileJpaRepository")
public interface UserProfileJpaRepository extends JpaRepository<UserProfile, String> {

    UserProfile findByType(String type);

    List<UserProfile> findAllByUsername(String username);
}