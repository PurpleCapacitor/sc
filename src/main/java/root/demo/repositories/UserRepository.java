package root.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import root.demo.model.users.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	
	public User findByUsername(String username);	

}
