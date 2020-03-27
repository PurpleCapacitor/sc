package root.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import root.demo.model.enums.OrderStatus;
import root.demo.model.enums.OrderType;
import root.demo.model.payment.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByBuyerUsername(String username);

	Order findByMerchantIdAndBuyerUsernameAndTypeAndStatus(String merchantId, String buyerUsername, OrderType type,
			OrderStatus status);

}
