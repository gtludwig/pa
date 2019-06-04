package ie.gtludwig.pa.model;

import ie.gtludwig.pa.model.generic.BasePojo;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Entity
@Table(name = "pa_user")
public class User extends BasePojo {

    private static final long serialVersionUID = 1L;

//    @NotEmpty
//    @Column(name = "username", unique = true, nullable = false)
//    private String username;

    @NotEmpty
    @Column(name = "email", unique = true, nullable = false)
    @Email(message="{errors.invalid_email}")
    private String email;

    @NotEmpty
    @Column(name = "password", nullable = false)
    private String password;

    @Transient
    private String passwordConfirm;

    @NotEmpty
    @Column(name = "firstName", nullable = false)
    private String firstName;

    @NotEmpty
    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "enabled")
    private boolean enabled = false;

    @NotEmpty
    @Column(name = "state", nullable = false)
    private String state = UserState.ACTIVE.getState();

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinTable(name = "pa_user2userProfile",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "userProfileId"))
    private Set<UserProfile> userProfileSet;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "pa_users_roles",
//            joinColumns = @JoinColumn(name = "userId", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "roleId", referencedColumnName = "id"))
//    private Collection<Role> roles;

//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }

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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

//    public Collection<Role> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(Collection<Role> roles) {
//        this.roles = roles;
//    }
}
