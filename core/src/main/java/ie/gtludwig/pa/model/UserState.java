package ie.gtludwig.pa.model;

public enum UserState {

    ACTIVE("status.active"),
    INACTIVE("status.inactive"),
    LOCKED("status.locked");

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
