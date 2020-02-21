package root.demo.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import root.demo.model.es.LocationIndex;

@Repository
public interface LocationESRepository extends ElasticsearchRepository<LocationIndex, String> {

	LocationIndex findByName(String name);
}
