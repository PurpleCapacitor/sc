package root.demo.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import root.demo.model.users.User;

@Entity
public class ScientificArea {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column
	private String name;

	@ManyToMany(mappedBy = "scientificAreas")
	private List<Magazine> magazines = new ArrayList<>();

	@ManyToMany(mappedBy = "scientificAreas")
	private List<User> users = new ArrayList<>();

	public ScientificArea(long id, String name, List<Magazine> magazines, List<User> users) {
		super();
		this.id = id;
		this.name = name;
		this.magazines = magazines;
		this.users = users;
	}

	public ScientificArea() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Magazine> getMagazines() {
		return magazines;
	}

	public void setMagazines(List<Magazine> magazines) {
		this.magazines = magazines;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}
