package ie.gtludwig.pa.service;

import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.model.VerificationToken;
import ie.gtludwig.pa.service.generic.CrudService;

public interface VerificationTokenService extends CrudService<VerificationToken> {

    VerificationToken findByToken(String verificationToken);

    VerificationToken findByUser(User user);

    VerificationToken createVerificationTokenForNewRegisteredUser(User user);

    void createVerificationTokenForUser(User user, String token);

    void delete(VerificationToken verificationToken);

    VerificationToken generateNewVerificationToken(final String existingVerificationToken);

}
