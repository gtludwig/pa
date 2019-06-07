package ie.gtludwig.pa.service;

public interface MailContentBuilderService {

    String build(String templatePath, String message);

    String buildHTMLTemplate(String message);
}
