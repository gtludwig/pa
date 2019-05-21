package ie.gtludwig.pa.dao;

import ie.gtludwig.pa.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleJpaRepository extends JpaRepository<Rule, String> {

    Rule findByDescription(String description);
}
