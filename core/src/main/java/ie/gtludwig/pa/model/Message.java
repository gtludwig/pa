package ie.gtludwig.pa.model;

import ie.gtludwig.pa.model.generic.BasePojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "pa_message")
public class Message extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Column(name = "content", nullable = false)
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
