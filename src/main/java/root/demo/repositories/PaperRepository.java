package root.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import root.demo.model.Paper;

@Repository
public interface PaperRepository extends JpaRepository<Paper, String> {
	public Paper findByTitle(String title);
	public Paper findByFile(String file);
}
