package ie.gtludwig.pa.model;

public enum UserProfileType {

    USER("USER"),
    ADMIN("ADMIN"),
    SPECIALIST("SPECIALIST");

    String type;

    private UserProfileType(String userProfileType) {
        this.type = userProfileType;
    }

    public String getUserProfileType() {
        return type;
    }
}
