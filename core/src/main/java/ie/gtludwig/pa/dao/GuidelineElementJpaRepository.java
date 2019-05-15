package ie.gtludwig.pa.dao;

import ie.gtludwig.pa.model.Guideline;
import ie.gtludwig.pa.model.GuidelineElement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuidelineElementJpaRepository extends JpaRepository<GuidelineElement, String> {

    List<GuidelineElement> findAllByGuideline(Guideline guideline);

}
