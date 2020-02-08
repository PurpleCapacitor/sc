package root.demo.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UnfinishedReviewDTO implements Serializable {

	private int numberOfUnfinishedReviews;

	public UnfinishedReviewDTO() {

	}

	public int getNumberOfUnfinishedReviews() {
		return numberOfUnfinishedReviews;
	}

	public void setNumberOfUnfinishedReviews(int numberOfUnfinishedReviews) {
		this.numberOfUnfinishedReviews = numberOfUnfinishedReviews;
	}

}
