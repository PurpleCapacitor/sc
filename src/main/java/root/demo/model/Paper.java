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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import root.demo.model.users.User;
import root.demo.model.users.UserDetails;

@Entity
public class Paper {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column
	private String title;

	@Column
	private String keywords;

	@Column
	private String paperAbstract;

	@Column
	@OneToMany
	private List<UserDetails> coAuthors = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "scArea_id")
	private ScientificArea scArea;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "author_id")
	private User author;

	@ManyToOne
	@JoinColumn(name = "magazine_id")
	private Magazine magazine;

	@Column
	private String file;

	@OneToMany(mappedBy = "paper")
	private List<Review> reviews = new ArrayList<>();

	@Column
	private String doi;

	public Paper() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getPaperAbstract() {
		return paperAbstract;
	}

	public void setPaperAbstract(String paperAbstract) {
		this.paperAbstract = paperAbstract;
	}

	public List<UserDetails> getCoAuthors() {
		return coAuthors;
	}

	public void setCoAuthors(List<UserDetails> coAuthors) {
		this.coAuthors = coAuthors;
	}

	public ScientificArea getScArea() {
		return scArea;
	}

	public void setScArea(ScientificArea scArea) {
		this.scArea = scArea;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Magazine getMagazine() {
		return magazine;
	}

	public void setMagazine(Magazine magazine) {
		this.magazine = magazine;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public String getDoi() {
		return doi;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}

}
