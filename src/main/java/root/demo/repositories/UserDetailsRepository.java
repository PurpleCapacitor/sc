package root.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import root.demo.model.users.UserDetails;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, String>{
	
	public UserDetails findByEmail(String email);

}
