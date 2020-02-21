package root.demo.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import root.demo.model.es.IndexUnit;

@Repository
public interface ESRepository extends ElasticsearchRepository<IndexUnit, String> {
	
	IndexUnit findByFile(String file);

}
