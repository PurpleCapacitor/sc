package root.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import root.demo.model.Edition;

@Repository
public interface EditionRepository extends JpaRepository<Edition, Long> {
	
	Edition findByName(String name);
}
