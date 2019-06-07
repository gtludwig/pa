package ie.gtludwig.pa.model;

import ie.gtludwig.pa.model.generic.BasePojo;

import java.util.Map;

//@Entity
//@Table(name = "pa_mail")
public class Mail extends BasePojo {

    private static final long serialVersionUID = 1L;

    private String from;

    private String to;

    private String subject;

    private Map<String, Object> model;

    public Mail() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
}
