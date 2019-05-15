package ie.gtludwig.pa.model;

public enum ProjectState {

    DRAFT("state.draft"),
    OPENED("state.opened"),
    UNDER_ANALYSIS("state.under_analysis"),
    REJECTED("state.rejected"),
    APPROVED("state.success"),
    CLOSED("state.closed");

    private String state;

    private ProjectState(final String state) {this.state = state;}

    public String getState() { return this.state; }

    @Override
    public String toString(){
        return this.state;
    }

    public String getName(){
        return this.name();
    }
}