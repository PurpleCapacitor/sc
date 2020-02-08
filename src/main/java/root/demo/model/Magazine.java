package root.demo.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import root.demo.model.enums.MagazineType;
import root.demo.model.users.User;

@Entity
public class Magazine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column
	private String name;

	@Column
	private MagazineType type;

	@Column
	private long issn;

	@Column
	private Boolean activated = false;

	@ManyToOne(fetch = FetchType.EAGER)
	private User mainEditor;

	@ManyToMany
	@JsonIgnore
	@JoinTable(name = "mag_editors", joinColumns = @JoinColumn(name = "mag_id"), inverseJoinColumns = @JoinColumn(name = "editor_id"))
	private List<User> editors = new ArrayList<>();

	@ManyToMany
	@JsonIgnore
	@JoinTable(name = "mag_scientific_area", joinColumns = @JoinColumn(name = "mag_id"), inverseJoinColumns = @JoinColumn(name = "scA_id"))
	private List<ScientificArea> scientificAreas = new ArrayList<>();

	@ManyToMany
	@JsonIgnore
	@JoinTable(name = "mag_reviewer", joinColumns = @JoinColumn(name = "mag_id"), inverseJoinColumns = @JoinColumn(name = "reviewer_id"))
	private List<User> reviewers = new ArrayList<>();

	public Magazine() {
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
	
	public MagazineType getType() {
		return type;
	}

	public void setType(MagazineType type) {
		this.type = type;
	}

	public void setIssn(long issn) {
		this.issn = issn;
	}
	
	public Boolean getActivated() {
		return activated;
	}

	public void setActivated(Boolean activated) {
		this.activated = activated;
	}

	public User getMainEditor() {
		return mainEditor;
	}

	public void setMainEditor(User mainEditor) {
		this.mainEditor = mainEditor;
	}

	public List<User> getEditors() {
		return editors;
	}

	public void setEditors(List<User> editors) {
		this.editors = editors;
	}

	public List<ScientificArea> getScientificAreas() {
		return scientificAreas;
	}

	public void setScientificAreas(List<ScientificArea> scientificAreas) {
		this.scientificAreas = scientificAreas;
	}

	public List<User> getReviewers() {
		return reviewers;
	}

	public void setReviewers(List<User> reviewers) {
		this.reviewers = reviewers;
	}

}
