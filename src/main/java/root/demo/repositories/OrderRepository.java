package root.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import root.demo.model.payment.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
	
	

}
