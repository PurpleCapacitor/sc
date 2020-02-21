package root.demo.model.users;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import root.demo.model.Magazine;
import root.demo.model.ScientificArea;
import root.demo.model.enums.UserType;

@Entity
public class User {

	@Id
	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

	@OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "user_details_id", unique = true)
	private UserDetails userDetails;

	@Column
	private boolean activated = false;

	@Column
	private boolean wantsReviewer = false;

	@Column
	private UserType userType;

	@OneToMany(mappedBy = "mainEditor")
	private List<Magazine> magazinesMainEditor = new ArrayList<>();

	@ManyToMany(mappedBy = "editors")
	private List<Magazine> magazinesEditor = new ArrayList<>();

	@ManyToMany(mappedBy = "reviewers")
	private List<Magazine> magazinesReviewers = new ArrayList<>();

	@ManyToMany
	@JsonIgnore
	@JoinTable(name = "user_scArea", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "scArea_id"))
	private List<ScientificArea> scientificAreas = new ArrayList<>();

	public User(String username, String password, UserDetails userDetails) {
		super();
		this.username = username;
		this.password = password;
		this.userDetails = userDetails;
	}

	public User() {

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public boolean isWantsReviewer() {
		return wantsReviewer;
	}

	public void setWantsReviewer(boolean wantsReviewer) {
		this.wantsReviewer = wantsReviewer;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public List<Magazine> getMagazinesMainEditor() {
		return magazinesMainEditor;
	}

	public void setMagazinesMainEditor(List<Magazine> magazinesMainEditor) {
		this.magazinesMainEditor = magazinesMainEditor;
	}

	public List<Magazine> getMagazinesEditor() {
		return magazinesEditor;
	}

	public void setMagazinesEditor(List<Magazine> magazinesEditor) {
		this.magazinesEditor = magazinesEditor;
	}

	public List<Magazine> getMagazinesReviewers() {
		return magazinesReviewers;
	}

	public void setMagazinesReviewers(List<Magazine> magazinesReviewers) {
		this.magazinesReviewers = magazinesReviewers;
	}

	public List<ScientificArea> getScientificAreas() {
		return scientificAreas;
	}

	public void setScientificAreas(List<ScientificArea> scientificAreas) {
		this.scientificAreas = scientificAreas;
	}

}
