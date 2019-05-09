package ie.gtludwig.pa.model;

import ie.gtludwig.pa.model.generic.BasePojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "pa_userProfile")
public class UserProfile extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Column(name = "type", length = 15, unique = true, nullable = false)
    private String type = UserProfileType.USER.getUserProfileType();

    // Constructor used only for initial data loading, not used after
    public UserProfile() {}

    // Constructor used only for initial data loading, not used after
    public UserProfile(String type) {
        super();
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
