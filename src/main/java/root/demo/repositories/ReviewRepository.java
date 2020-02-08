package root.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import root.demo.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {

}
