package root.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import root.demo.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, String> {

}
