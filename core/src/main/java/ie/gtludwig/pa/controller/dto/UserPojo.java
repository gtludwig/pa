package ie.gtludwig.pa.controller.dto;

import ie.gtludwig.pa.model.UserProfile;
import ie.gtludwig.pa.model.UserState;

import java.util.Set;

public class UserPojo {

    private String id;

    private String username;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String state = UserState.ACTIVE.getState();

    private Set<UserProfile> userProfileSet;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
