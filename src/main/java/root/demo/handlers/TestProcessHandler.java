package root.demo.handlers;

import java.util.List;

import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.model.Magazine;
import root.demo.model.ScientificArea;
import root.demo.model.enums.MagazineType;
import root.demo.model.enums.UserType;
import root.demo.model.users.UserDetails;
import root.demo.repositories.MagazineRepository;
import root.demo.repositories.ScientificAreaRepository;
import root.demo.repositories.UserRepository;

@Service
public class TestProcessHandler implements ExecutionListener {
	@Autowired
	IdentityService identityService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ScientificAreaRepository scientificAreaRepository;

	@Autowired
	AuthorizationService authorizationService;

	@Autowired
	MagazineRepository magazineRepository;

	@Override
	public void notify(DelegateExecution execution) throws Exception {

		System.out.println("Poceo proces");

		List<ScientificArea> scAreas = scientificAreaRepository.findAll();
		List<root.demo.model.users.User> useri = userRepository.findAll();
		List<Magazine> magazines = magazineRepository.findAll();

		if (scAreas.isEmpty()) {
			ScientificArea sc1 = new ScientificArea();
			sc1.setName("math");
			ScientificArea sc2 = new ScientificArea();
			sc2.setName("biology");
			ScientificArea sc3 = new ScientificArea();
			sc3.setName("engineering");
			scientificAreaRepository.saveAndFlush(sc1);
			scientificAreaRepository.saveAndFlush(sc2);
			scientificAreaRepository.saveAndFlush(sc3);
		}

		if (useri.isEmpty()) {

			ScientificArea sc1 = scientificAreaRepository.findByName("math");
			ScientificArea sc2 = scientificAreaRepository.findByName("biology");
			ScientificArea sc3 = scientificAreaRepository.findByName("engineering");

			UserDetails userDetails = new UserDetails(1, "d@d", "da", "da", "da", "da");
			root.demo.model.users.User tester = new root.demo.model.users.User("test", "test", userDetails);
			tester.setActivated(true);
			tester.setUserType(UserType.admin);
			userRepository.save(tester);

			UserDetails userDetails2 = new UserDetails(2, "a@aa", "a", "a", "a", "a");
			root.demo.model.users.User editor1 = new root.demo.model.users.User("editor1", "demo", userDetails2);
			editor1.setActivated(true);
			editor1.setUserType(UserType.editor);
			editor1.getScientificAreas().add(sc1); // math
			sc1.getUsers().add(editor1);

			UserDetails userDetails3 = new UserDetails(3, "c@aja", "c", "ca", "ac", "ac");
			root.demo.model.users.User editor2 = new root.demo.model.users.User("editor2", "demo", userDetails3);
			editor2.setActivated(true);
			editor2.setUserType(UserType.editor);
			editor2.getScientificAreas().add(sc1); // math
			sc1.getUsers().add(editor2);

			UserDetails userDetails4 = new UserDetails(4, "a@bj", "bjf", "b", "b", "b");
			root.demo.model.users.User editor3 = new root.demo.model.users.User("editor3", "demo", userDetails4);
			editor3.setActivated(true);
			editor3.setUserType(UserType.editor);
			editor3.getScientificAreas().add(sc2); // biology
			sc2.getUsers().add(editor3);

			UserDetails userDetails5 = new UserDetails(5, "aaa@b", "fb", "bf", "bf", "bf");
			root.demo.model.users.User editor4 = new root.demo.model.users.User("editor4", "demo", userDetails5);
			editor4.setActivated(true);
			editor4.setUserType(UserType.editor);
			editor4.getScientificAreas().add(sc2); // biology
			sc2.getUsers().add(editor4);

			UserDetails userDetails6 = new UserDetails(6, "asaa@fb", "fsasb", "jhbf", "ghjbf", "bgjf");
			root.demo.model.users.User reviewer1 = new root.demo.model.users.User("reviewer1", "demo", userDetails6);
			reviewer1.setActivated(true);
			reviewer1.setUserType(UserType.reviewer);
			reviewer1.getScientificAreas().add(sc1); // math
			sc1.getUsers().add(reviewer1);

			UserDetails userDetails7 = new UserDetails(7, "aadcxa@b", "fsdb", "dgbf", "bghjf", "bgjf");
			root.demo.model.users.User reviewer2 = new root.demo.model.users.User("reviewer2", "demo", userDetails7);
			reviewer2.setActivated(true);
			reviewer2.setUserType(UserType.reviewer);
			reviewer2.getScientificAreas().add(sc1); // math
			sc1.getUsers().add(reviewer2);

			UserDetails userDetails8 = new UserDetails(8, "a@cxvxcvb", "fbfdhgd", "bfwghf", "bfqf", "bwehhf");
			root.demo.model.users.User reviewer3 = new root.demo.model.users.User("reviewer3", "demo", userDetails8);
			reviewer3.setActivated(true);
			reviewer3.setUserType(UserType.reviewer);
			reviewer3.getScientificAreas().add(sc2); // biology
			sc2.getUsers().add(reviewer3);

			UserDetails userDetails9 = new UserDetails(9, "a@cvb", "fbfdhgd", "bfqghf", "bqff", "qbhhf");
			root.demo.model.users.User reviewer4 = new root.demo.model.users.User("reviewer4", "demo", userDetails9);
			reviewer4.setActivated(true);
			reviewer4.setUserType(UserType.reviewer);
			reviewer4.getScientificAreas().add(sc2); // biology
			sc2.getUsers().add(reviewer4);

			UserDetails userDetails10 = new UserDetails(10, "aaa@cxvdsaxcvb", "fbfdvbhgd", "bfgvnhf", "bvzff", "qbhhf");
			root.demo.model.users.User reviewer5 = new root.demo.model.users.User("reviewer5", "demo", userDetails10);
			reviewer5.setActivated(true);
			reviewer5.setUserType(UserType.reviewer); // engineering
			reviewer5.getScientificAreas().add(sc3);
			sc3.getUsers().add(reviewer5);

			UserDetails userDetails11 = new UserDetails(11, "a@cp", "fbggd", "bfgvgnhf", "bvzfg", "qbhahf");
			root.demo.model.users.User author = new root.demo.model.users.User("author", "demo", userDetails11);
			author.setActivated(true);
			author.setUserType(UserType.author);
			
			UserDetails userDetails12 = new UserDetails(12, "aaa@cdb", "zbhgd", "vnhf", "bvzff", "qbhhf");
			root.demo.model.users.User reviewer6 = new root.demo.model.users.User("reviewer6", "demo", userDetails12);
			reviewer6.setActivated(true);
			reviewer6.setUserType(UserType.reviewer); // math
			reviewer6.getScientificAreas().add(sc1);
			sc1.getUsers().add(reviewer6);
			
			UserDetails userDetails13 = new UserDetails(13, "adaa@lb", "zhbhgd", "vnhjf", "bvjzff", "qbhjhf");
			root.demo.model.users.User reviewer7 = new root.demo.model.users.User("reviewer7", "demo", userDetails13);
			reviewer7.setActivated(true);
			reviewer7.setUserType(UserType.reviewer); // math
			reviewer7.getScientificAreas().add(sc1);
			sc1.getUsers().add(reviewer7);

			userRepository.saveAndFlush(editor1);
			userRepository.saveAndFlush(editor2);
			userRepository.saveAndFlush(editor3);
			userRepository.saveAndFlush(editor4);
			userRepository.saveAndFlush(reviewer1);
			userRepository.saveAndFlush(reviewer2);
			userRepository.saveAndFlush(reviewer3);
			userRepository.saveAndFlush(reviewer4);
			userRepository.saveAndFlush(reviewer5);
			userRepository.saveAndFlush(author);
			userRepository.saveAndFlush(reviewer6);
			userRepository.saveAndFlush(reviewer7);

		}

		if (magazines.isEmpty()) {

			Magazine m1 = new Magazine(); // math / engineering magazine open access
			root.demo.model.users.User mainEditor = userRepository.findByUsername("editor1");
			root.demo.model.users.User editor = userRepository.findByUsername("editor2");
			root.demo.model.users.User reviewer1 = userRepository.findByUsername("reviewer1");
			root.demo.model.users.User reviewer2 = userRepository.findByUsername("reviewer2");
			root.demo.model.users.User reviewer5 = userRepository.findByUsername("reviewer5");
			root.demo.model.users.User reviewer6 = userRepository.findByUsername("reviewer6");
			root.demo.model.users.User reviewer7 = userRepository.findByUsername("reviewer7");
			ScientificArea sc = scientificAreaRepository.findByName("math");
			ScientificArea sc3 = scientificAreaRepository.findByName("engineering");

			m1.setActivated(true);
			m1.setId(1);
			m1.setIssn(11);
			m1.setName("magazine1");
			m1.setType(MagazineType.openAccess);
			m1.setMainEditor(mainEditor);
			m1.getEditors().add(editor);
			m1.getScientificAreas().add(sc);
			m1.getScientificAreas().add(sc3);
			m1.getReviewers().add(reviewer1);
			m1.getReviewers().add(reviewer2);
			m1.getReviewers().add(reviewer5);
			m1.getReviewers().add(reviewer6);
			m1.getReviewers().add(reviewer7);

			mainEditor.getMagazinesMainEditor().add(m1);
			editor.getMagazinesEditor().add(m1);
			sc.getMagazines().add(m1);
			sc3.getMagazines().add(m1);
			reviewer1.getMagazinesReviewers().add(m1);
			reviewer2.getMagazinesReviewers().add(m1);
			reviewer5.getMagazinesReviewers().add(m1);
			reviewer6.getMagazinesReviewers().add(m1);
			reviewer7.getMagazinesReviewers().add(m1);

			magazineRepository.saveAndFlush(m1);

			Magazine m2 = new Magazine(); // biology magazine paid access
			root.demo.model.users.User mainEditorMag2 = userRepository.findByUsername("editor3");
			root.demo.model.users.User editorMag2 = userRepository.findByUsername("editor4");
			root.demo.model.users.User reviewer1Mag2 = userRepository.findByUsername("reviewer3");
			root.demo.model.users.User reviewer2Mag2 = userRepository.findByUsername("reviewer4");
			ScientificArea sc2 = scientificAreaRepository.findByName("biology");

			m2.setActivated(true);
			m2.setId(2);
			m2.setIssn(12);
			m2.setName("magazine2");
			m2.setType(MagazineType.paidAccess);
			m2.setMainEditor(mainEditorMag2);
			m2.getEditors().add(editorMag2);
			m2.getScientificAreas().add(sc2);
			m2.getReviewers().add(reviewer1Mag2);
			m2.getReviewers().add(reviewer2Mag2);

			mainEditorMag2.getMagazinesMainEditor().add(m2);
			editorMag2.getMagazinesEditor().add(m2);
			sc2.getMagazines().add(m2);
			reviewer1Mag2.getMagazinesReviewers().add(m2);
			reviewer2Mag2.getMagazinesReviewers().add(m2);

			magazineRepository.saveAndFlush(m2);

			/*
			 * Magazine m3 = new Magazine(); // biology magazine paid access no editors nor
			 * reviewers root.demo.model.users.User mainEditorMag3 =
			 * userRepository.findByUsername("editor3");
			 * 
			 * root.demo.model.users.User editorMag2 =
			 * userRepository.findByUsername("editor4"); root.demo.model.users.User
			 * reviewer1Mag2 = userRepository.findByUsername("reviewer3");
			 * root.demo.model.users.User reviewer2Mag2 =
			 * userRepository.findByUsername("reviewer4");
			 * 
			 * 
			 * m3.setActivated(true); m3.setId(3); m3.setIssn(13); m3.setName("magazine3");
			 * m3.setType(MagazineType.paidAccess); m3.setMainEditor(mainEditorMag3);
			 * //m2.getEditors().add(editorMag2); m3.getScientificAreas().add(sc2);
			 * 
			 * m2.getReviewers().add(reviewer1Mag2); m2.getReviewers().add(reviewer2Mag2);
			 * 
			 * 
			 * mainEditorMag3.getMagazinesMainEditor().add(m3);
			 * //editorMag2.getMagazinesEditor().add(m2); sc2.getMagazines().add(m3);
			 * 
			 * reviewer1Mag2.getMagazinesReviewers().add(m2);
			 * reviewer2Mag2.getMagazinesReviewers().add(m2);
			 * 
			 * magazineRepository.saveAndFlush(m3);
			 */

		}

		List<User> users = identityService.createUserQuery()
				.userIdIn("editor1", "editor2", "editor3", "reviewer1", "reviewer2", "reviewer3", "author", "reviewer5",
						"reviewer6", "reviewer7")
				.list();

		if (users.isEmpty()) {

			User user1 = identityService.newUser("editor1"); // glavni editor
			user1.setEmail("pera@mail.com");
			user1.setFirstName("Pera");
			user1.setLastName("Peric");
			user1.setPassword("pass");
			identityService.saveUser(user1);

			User user2 = identityService.newUser("editor2");
			user2.setEmail("persa@mail.com");
			user2.setFirstName("Nera");
			user2.setLastName("Peric");
			user2.setPassword("pass");
			identityService.saveUser(user2);
			
			User user10 = identityService.newUser("editor3");
			user10.setEmail("perjdsfgghjsa@mail.com");
			user10.setFirstName("Srra");
			user10.setLastName("Peric");
			user10.setPassword("pass");
			identityService.saveUser(user10);

			// revieweri

			User user3 = identityService.newUser("reviewer1");
			user3.setEmail("zika@mail.com");
			user3.setFirstName("Zika");
			user3.setLastName("Zikic");
			user3.setPassword("pass");
			identityService.saveUser(user3);

			User user4 = identityService.newUser("reviewer2");
			user4.setEmail("joka@mail.com");
			user4.setFirstName("Joka");
			user4.setLastName("Jokic");
			user4.setPassword("pass");
			identityService.saveUser(user4);

			User user5 = identityService.newUser("reviewer3");
			user5.setEmail("koka@mail.com");
			user5.setFirstName("Koka");
			user5.setLastName("Kokic");
			user5.setPassword("pass");
			identityService.saveUser(user5);

			User user7 = identityService.newUser("reviewer5");
			user7.setEmail("grr@mail.com");
			user7.setFirstName("Nom");
			user7.setLastName("Pom");
			user7.setPassword("pass");
			identityService.saveUser(user7);
			
			User user8 = identityService.newUser("reviewer6");
			user8.setEmail("zikhdaa@mail.com");
			user8.setFirstName("Zisdka");
			user8.setLastName("Zikissc");
			user8.setPassword("pass");
			identityService.saveUser(user8);
			
			User user9 = identityService.newUser("reviewer7");
			user9.setEmail("s@ail.com");
			user9.setFirstName("skks");
			user9.setLastName("ghasd");
			user9.setPassword("pass");
			identityService.saveUser(user9);

			// autor

			User user6 = identityService.newUser("author");
			user6.setEmail("mika@mail.com");
			user6.setFirstName("Mika");
			user6.setLastName("Mikic");
			user6.setPassword("pass");
			identityService.saveUser(user6);

			// Grupe

			/*
			 * Group editorsGroup = identityService.newGroup("editors");
			 * editorsGroup.setName("Editors"); editorsGroup.setType("WORKFLOW");
			 * identityService.saveGroup(editorsGroup);
			 * 
			 * Group reviewersGroup = identityService.newGroup("reviewers");
			 * reviewersGroup.setName("Reviewers"); reviewersGroup.setType("WORKFLOW");
			 * identityService.saveGroup(reviewersGroup);
			 * 
			 * identityService.createMembership("editor1", "editors");
			 * identityService.createMembership("editor2", "editors");
			 * 
			 * identityService.createMembership("reviewer1", "reviewers");
			 * identityService.createMembership("reviewer2", "reviewers");
			 * identityService.createMembership("reviewer3", "reviewers");
			 * identityService.createMembership("reviewer5", "reviewers");
			 */

		}

	}
}