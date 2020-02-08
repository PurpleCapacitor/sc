package root.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import root.demo.model.users.User;

@Entity
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column
	private String reviewerAuthorComment;

	@Column
	private String reviewerComment;

	@Column
	private String suggestion;

	@ManyToOne
	@JoinColumn(name = "reviewer_id")
	private User reviewer;

	@ManyToOne(fetch = FetchType.EAGER)
	private Paper paper;

	public Review() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getReviewerAuthorComment() {
		return reviewerAuthorComment;
	}

	public void setReviewerAuthorComment(String reviewerAuthorComment) {
		this.reviewerAuthorComment = reviewerAuthorComment;
	}

	public String getReviewerComment() {
		return reviewerComment;
	}

	public void setReviewerComment(String reviewerComment) {
		this.reviewerComment = reviewerComment;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	public User getReviewer() {
		return reviewer;
	}

	public void setReviewer(User reviewer) {
		this.reviewer = reviewer;
	}

	public Paper getPaper() {
		return paper;
	}

	public void setPaper(Paper paper) {
		this.paper = paper;
	}

}
