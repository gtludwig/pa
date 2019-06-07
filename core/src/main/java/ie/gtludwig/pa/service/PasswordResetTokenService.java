package ie.gtludwig.pa.service;

import ie.gtludwig.pa.model.PasswordResetToken;
import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.service.generic.CrudService;

public interface PasswordResetTokenService extends CrudService<PasswordResetToken> {

    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUser(User user);

    void createPasswordResetTokenForUser(User user, String token);

    void delete(PasswordResetToken passwordResetToken);

    void deletePasswordResetTokenByUser(User user);
}
