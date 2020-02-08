package root.demo.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ReviewDTO implements Serializable {


	private String reviewerAuthorComment;
	private String reviewerComment;
	private String suggestion;
	private String reviewer;
	
	public ReviewDTO() {
		
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

	public String getReviewer() {
		return reviewer;
	}

	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}
	
	

}
