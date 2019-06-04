package ie.gtludwig.pa.service.impl;

import ie.gtludwig.pa.dao.VerificationTokenJpaRepository;
import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.model.VerificationToken;
import ie.gtludwig.pa.service.VerificationTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service(value = "verificationTokenService")
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private static Logger logger = LoggerFactory.getLogger(VerificationTokenServiceImpl.class);

    private static final int EXPIRATION = 60 * 24;

    @Autowired
    private VerificationTokenJpaRepository verificationTokenJpaRepository;

    @Override
    public VerificationToken findByToken(String verificationToken) {
        return verificationTokenJpaRepository.findByToken(verificationToken);
    }

    @Override
    public VerificationToken findByUser(User user) {
        return verificationTokenJpaRepository.findByUser(user);
    }

    @Override
    public VerificationToken createVerificationTokenForNewRegisteredUser(User user) {
        VerificationToken verificationToken = new VerificationToken(UUID.randomUUID().toString(), user);
        return verificationTokenJpaRepository.saveAndFlush(verificationToken);
    }

    @Override
    public void createVerificationTokenForUser(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user, calculateExpiryDate(EXPIRATION));
        myToken = verificationTokenJpaRepository.saveAndFlush(myToken);
//        save(myToken);
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(calendar.getTime().getTime());
    }

    @Override
    public void delete(VerificationToken verificationToken) {
        verificationTokenJpaRepository.delete(verificationToken);
    }

    @Override
    public VerificationToken generateNewVerificationToken(String existingVerificationToken) {
        VerificationToken verificationToken = findByToken(existingVerificationToken);
        verificationToken.updateToken(UUID.randomUUID().toString());
        return verificationTokenJpaRepository.saveAndFlush(verificationToken);
    }

    @Override
    public VerificationToken findById(String id) {
        return null;
    }

    @Override
    public void save(VerificationToken pojo) {

    }

    @Override
    public void remove(String id) {

    }

    @Override
    public List<VerificationToken> findAll() {
        return null;
    }
}
