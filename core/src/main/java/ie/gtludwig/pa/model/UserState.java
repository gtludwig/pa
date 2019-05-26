package ie.gtludwig.pa.model;

public enum UserState {

    ACTIVE("user.status-active"),
    INACTIVE("user.status-inactive"),
    LOCKED("user.status-locked");

    private String state;

    private UserState(final String state){
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
