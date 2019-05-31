package ie.gtludwig.pa.model;

public enum UserState {

    ACTIVE("user.status-active"),
    INACTIVE("user.status-inactive"),
    INVITED("user.status-invited"),
    SUGGESTED("user.status-suggested"),
    LOCKED("user.status-locked");

    private String state;

    UserState(final String state) {
        this.state = state;
    }

    public String getState(){
        return this.state;
    }

    @Override
    public String toString(){
        return this.state;
    }

    public String getName(){
        return this.name();
    }
}
