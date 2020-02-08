package root.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import root.demo.model.ProcessInstanceData;

@Repository
public interface ProcessInstanceDataRepository extends JpaRepository<ProcessInstanceData, String>{
	
	public ProcessInstanceData findTopByOrderByIdDesc();

}
