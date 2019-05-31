package ie.gtludwig.pa.service.impl;

import ie.gtludwig.pa.dao.VerificationTokenJpaRepository;
import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.model.VerificationToken;
import ie.gtludwig.pa.service.VerificationTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service(value = "verificationTokenServiceImpl")
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private static Logger logger = LoggerFactory.getLogger(VerificationTokenServiceImpl.class);

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
    public void createVerificationTokenForUser(User user, String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        save(myToken);
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
