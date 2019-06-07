package ie.gtludwig.pa.service.impl;

import ie.gtludwig.pa.dao.PasswordResetTokenJpaRepository;
import ie.gtludwig.pa.model.PasswordResetToken;
import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.service.PasswordResetTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service(value = "passwordResetTokenService")
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private static final int EXPIRATION = 60 * 24;
    private static Logger logger = LoggerFactory.getLogger(PasswordResetTokenServiceImpl.class);
    @Autowired
    private PasswordResetTokenJpaRepository passwordResetTokenJpaRepository;

    @Override
    public PasswordResetToken findById(String id) {
        return passwordResetTokenJpaRepository.getOne(id);
    }

    @Override
    public PasswordResetToken findByToken(String token) {
        return passwordResetTokenJpaRepository.findByToken(token);
    }

    @Override
    public PasswordResetToken findByUser(User user) {
        return passwordResetTokenJpaRepository.findByUser(user);
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user, calculateExpiryDate(EXPIRATION));
        myToken = passwordResetTokenJpaRepository.saveAndFlush(myToken);
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(calendar.getTime().getTime());
    }

    @Override
    public void delete(PasswordResetToken passwordResetToken) {
        passwordResetTokenJpaRepository.delete(passwordResetToken);
    }

    @Override
    public void deletePasswordResetTokenByUser(User user) {
        delete(passwordResetTokenJpaRepository.findByUser(user));
    }

    @Override
    public void save(PasswordResetToken pojo) {
        passwordResetTokenJpaRepository.save(pojo);
    }

    @Override
    public void remove(String id) {
        passwordResetTokenJpaRepository.delete(findById(id));
    }

    @Override
    public List<PasswordResetToken> findAll() {
        return passwordResetTokenJpaRepository.findAll();
    }
}
