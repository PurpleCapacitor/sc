package root.demo.services.camunda.reviewProcess;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.dto.FormSubmissionDto;
import root.demo.model.Magazine;
import root.demo.model.Paper;
import root.demo.model.Review;
import root.demo.model.ScientificArea;
import root.demo.model.users.User;
import root.demo.repositories.MagazineRepository;
import root.demo.repositories.PaperRepository;
import root.demo.repositories.ReviewRepository;
import root.demo.repositories.ScientificAreaRepository;
import root.demo.repositories.UserRepository;

@Service
public class ReviewService implements JavaDelegate {

	@Autowired
	ReviewRepository reviewRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PaperRepository paperRepository;

	@Autowired
	MagazineRepository magazineRepository;

	@Autowired
	ScientificAreaRepository scientificAreaRepository;

	public void addReview(String title, List<FormSubmissionDto> dto) {
		Review review = new Review();
		Paper paper = paperRepository.findByTitle(title);
		review.setPaper(paper);
		User reviewer = null;

		for (FormSubmissionDto d : dto) {
			if (d.getFieldId().equals("reviewerAuthorComment")) {
				review.setReviewerAuthorComment(d.getFieldValue());
			}
			if (d.getFieldId().equals("reviewerComment")) {
				review.setReviewerComment(d.getFieldValue());
			}
			if (d.getFieldId().equals("suggestion")) {
				review.setSuggestion(d.getFieldValue());
			}
			if (d.getFieldId().equals("reviewerUsername")) {
				reviewer = userRepository.findByUsername(d.getFieldValue());
			}
		}
		review.setReviewer(reviewer);
		paper.getReviews().add(review);
		reviewRepository.saveAndFlush(review);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		int reviewersListSize = (int) execution.getVariable("reviewersListSize");
		int noOfFinishedReviews;
		
		try {
			noOfFinishedReviews = (int) execution.getVariable("noOfFinishedReviews");
		} catch(Exception e) {
			System.out.println("No reviewer completed");
			noOfFinishedReviews = 0;
		}
		
		int reviewsUnfinished = reviewersListSize - noOfFinishedReviews;
		execution.setVariable("reviewsUnfinished", reviewsUnfinished);

		List<FormSubmissionDto> paper = (List<FormSubmissionDto>) execution.getVariable("paperDetails");
		ScientificArea sc = new ScientificArea();
		Magazine m = new Magazine();

		for (FormSubmissionDto d : paper) {
			if (d.getFieldId().equals("scientificArea")) {
				sc = scientificAreaRepository.findByName(d.getFieldValue());
			}
			if (d.getFieldId().equals("magazine")) {
				m = magazineRepository.findByName(d.getFieldValue());
			}
		}

		List<User> scUsers = sc.getUsers();
		List<User> magazineReviewers = m.getReviewers();
		List<String> matchedUsers = new ArrayList<String>();
		if (!magazineReviewers.isEmpty()) {
			for (User magEditor : magazineReviewers) {
				for (User u : scUsers) {
					if (magEditor.getUsername().equals(u.getUsername())) {
						matchedUsers.add(u.getUsername());
					}
				}
			}
		}
		List<String> previouslyAssigned = (List<String>) execution.getVariable("reviewersList");
		List<String> toRemove = new ArrayList<String>();
		for (String assigned : previouslyAssigned) {
			for (String username : matchedUsers) {
				if (username.equals(assigned)) {
					toRemove.add(username);
				}
			}
		}
		
		matchedUsers.removeAll(toRemove);

		execution.setVariable("availableAdditionalReviewers", matchedUsers);

	}

}
