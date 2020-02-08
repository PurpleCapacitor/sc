package root.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import root.demo.model.Magazine;

@Repository
public interface MagazineRepository extends JpaRepository<Magazine, String> {
	
	public Magazine findByName(String name);

}
