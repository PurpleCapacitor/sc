package root.demo.services.es;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.model.es.LocationIndex;
import root.demo.repositories.LocationESRepository;

@Service
public class LocationIndexer {
	
	@Autowired
	LocationESRepository repository;
	
	public void add(LocationIndex locationIndex) {
		repository.index(locationIndex);
	}
	
	public void update(LocationIndex locationIndex) {
		repository.save(locationIndex);
	}
	
	public void delete(String name) {
		repository.deleteById(name);
	}

}
