package ie.gtludwig.pa.model;

public enum ProjectState {

    DRAFT("project.state-draft"),
    OPEN("project.state-open"),
    UNDER_ANALYSIS("project.state-under_analysis"),
    REJECTED("project.state-rejected"),
    APPROVED("project.state-approved"),
    CLOSED("project.state-closed");

    private String state;

    ProjectState(final String state) {this.state = state;}

    public String getState() { return this.state; }

    @Override
    public String toString(){
        return this.state;
    }

    public String getName(){
        return this.name();
    }
}