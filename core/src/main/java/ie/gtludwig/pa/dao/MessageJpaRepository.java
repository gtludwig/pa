package ie.gtludwig.pa.dao;

import ie.gtludwig.pa.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageJpaRepository extends JpaRepository<Message, String> {
}
