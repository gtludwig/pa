package ie.gtludwig.pa.model;

public enum UserProfileType {

    USER("USER"),
    SPECIALIST("SPECIALIST"),
    SPONSOR("SPONSOR"),
    ADMIN("ADMIN");

    String type;

    UserProfileType(String userProfileType) {
        this.type = userProfileType;
    }

    public String getUserProfileType() {
        return type;
    }
}
