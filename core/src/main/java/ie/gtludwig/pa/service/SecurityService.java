package ie.gtludwig.pa.service;

public interface SecurityService {

    String findLoggedInUsername();

    void autologin(String username, String password);
}
