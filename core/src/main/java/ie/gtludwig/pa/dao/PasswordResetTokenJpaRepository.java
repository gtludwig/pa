package ie.gtludwig.pa.dao;

import ie.gtludwig.pa.model.PasswordResetToken;
import ie.gtludwig.pa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenJpaRepository extends JpaRepository<PasswordResetToken, String> {

    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUser(User user);
}
