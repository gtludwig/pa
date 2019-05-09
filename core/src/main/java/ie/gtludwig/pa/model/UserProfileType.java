package ie.gtludwig.pa.model;

public enum UserProfileType {

    USER("User"),
    ADMIN("Admin"),
    SPECIALIST("Specialist");

    String type;

    private UserProfileType(String userProfileType) {
        this.type = userProfileType;
    }

    public String getUserProfileType() {
        return type;
    }
}
