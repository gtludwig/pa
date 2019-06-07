package ie.gtludwig.pa.controller.dto;

import ie.gtludwig.pa.model.UserProfile;
import ie.gtludwig.pa.model.UserState;

import java.util.Set;

//@PasswordMatches
public class UserPojo {

    private String id;

    private String email;

    private String password;

    private String passwordConfirm;

    private String firstName;

    private String lastName;

    private String state = UserState.INACTIVE.getState();

    private Set<UserProfile> userProfileSet;

    private boolean accountOwner;

    private boolean enabled;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Set<UserProfile> getUserProfileSet() {
        return userProfileSet;
    }

    public void setUserProfileSet(Set<UserProfile> userProfileSet) {
        this.userProfileSet = userProfileSet;
    }

    public boolean isAccountOwner() {
        return accountOwner;
    }

    public void setAccountOwner(boolean accountOwner) {
        this.accountOwner = accountOwner;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
