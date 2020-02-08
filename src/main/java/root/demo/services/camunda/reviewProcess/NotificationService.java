package root.demo.services.camunda.reviewProcess;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import root.demo.model.users.User;
import root.demo.repositories.UserRepository;

@Service
public class NotificationService implements JavaDelegate {

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	UserRepository userRepository;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String message = (String) execution.getVariableLocal("message");
		String mainEditorUsername = (String) execution.getVariable("mainEditorUsername");
		String authorUsername = (String) execution.getVariable("authorUsername");
		String pickedEditorUsername = (String) execution.getVariable("assignedEditor");
		if (message.equals("newPaper")) {
			newPaper(mainEditorUsername, authorUsername);
		}
		if (message.equals("basicReviewDeny")) {
			basicReviewDeny(authorUsername);
		}
		if (message.equals("formatPaper")) {
			formatPaper(authorUsername);
		}
		if (message.equals("technicalReason")) {
			technicalReasonEnd(authorUsername);
		}
		if (message.equals("editorPick")) {
			assignedEditor(pickedEditorUsername);
		}
		if (message.equals("reviewersDeadline")) {
			reviewersDeadline(pickedEditorUsername);
		}
		if (message.equals("rework")) {
			reworkPaper(authorUsername);
		}
		if (message.equals("accepted")) {
			accepted(authorUsername);
		}
		if (message.equals("denied")) {
			denied(authorUsername);
		}
	}

	private void denied(String authorUsername) {
		User author = userRepository.findByUsername(authorUsername);

		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(author.getUserDetails().getEmail());
		mail.setFrom("upp@sbb.rs");
		mail.setSubject("Paper denied");
		mail.setText("Your paper has been denied.");
		// javaMailSender.send(mail);
		System.out.println(mail.getText());
	}

	private void accepted(String authorUsername) {
		User author = userRepository.findByUsername(authorUsername);

		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(author.getUserDetails().getEmail());
		mail.setFrom("upp@sbb.rs");
		mail.setSubject("Paper accepted");
		mail.setText("Your paper has been accepted.");
		// javaMailSender.send(mail);
		System.out.println(mail.getText());

	}

	private void reworkPaper(String authorUsername) {
		User author = userRepository.findByUsername(authorUsername);

		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(author.getUserDetails().getEmail());
		mail.setFrom("upp@sbb.rs");
		mail.setSubject("Rework paper");
		mail.setText("You have to rework paper.");
		// javaMailSender.send(mail);
		System.out.println(mail.getText());

	}

	private void reviewersDeadline(String pickedEditorUsername) {
		User assignedEditor = userRepository.findByUsername(pickedEditorUsername);

		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(assignedEditor.getUserDetails().getEmail());
		mail.setFrom("upp@sbb.rs");
		mail.setSubject("Pick different reviewer");
		mail.setText("One of the reviewers didn't deliever, please pick a different reviewer.");
		// javaMailSender.send(mail);
		System.out.println(mail.getText());

	}

	private void assignedEditor(String pickedEditorUsername) {
		User assignedEditor = userRepository.findByUsername(pickedEditorUsername);

		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(assignedEditor.getUserDetails().getEmail());
		mail.setFrom("upp@sbb.rs");
		mail.setSubject("Paper assigned");
		mail.setText("Paper for review has been assigned to you.");
		// javaMailSender.send(mail);
		System.out.println(mail.getText());

	}

	private void technicalReasonEnd(String authorUsername) {
		User author = userRepository.findByUsername(authorUsername);

		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(author.getUserDetails().getEmail());
		mail.setFrom("upp@sbb.rs");
		mail.setSubject("Time allowed passed");
		mail.setText("You didn't complete in time.");
		// javaMailSender.send(mail);
		System.out.println(mail.getText());

	}

	private void formatPaper(String authorUsername) {
		User author = userRepository.findByUsername(authorUsername);

		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(author.getUserDetails().getEmail());
		mail.setFrom("upp@sbb.rs");
		mail.setSubject("Format paper");
		mail.setText("You have to format your paper.");
		// javaMailSender.send(mail);
		System.out.println(mail.getText());

	}

	private void newPaper(String mainEditorUsername, String authorUsername) {
		User mainEditor = userRepository.findByUsername(mainEditorUsername);
		User author = userRepository.findByUsername(authorUsername);

		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(mainEditor.getUserDetails().getEmail());
		mail.setFrom("upp@sbb.rs");
		mail.setSubject("New paper");
		mail.setText("New paper submitted in your magazine.");
		// javaMailSender.send(mail);
		System.out.println("Main editor email SENT");

		SimpleMailMessage mail2 = new SimpleMailMessage();
		mail2.setTo(author.getUserDetails().getEmail());
		mail2.setFrom("upp@sbb.rs");
		mail2.setSubject("New paper");
		mail2.setText("New paper submitted");
		// javaMailSender.send(mail2);
		System.out.println("Author email SENT");
	}

	private void basicReviewDeny(String authorUsername) {
		User author = userRepository.findByUsername(authorUsername);

		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(author.getUserDetails().getEmail());
		mail.setFrom("upp@sbb.rs");
		mail.setSubject("Paper denied");
		mail.setText("Paper didn't pass basic review.");
		// javaMailSender.send(mail);
		System.out.println(mail.getText());
	}
}
