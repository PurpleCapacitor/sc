package root.demo.services.es;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.handlers.DocumentHandler;
import root.demo.handlers.PDFHandler;
import root.demo.handlers.TextDocHandler;
import root.demo.handlers.Word2007Handler;
import root.demo.handlers.WordHandler;
import root.demo.model.Magazine;
import root.demo.model.Paper;
import root.demo.model.ScientificArea;
import root.demo.model.es.IndexUnit;
import root.demo.model.users.User;
import root.demo.model.users.UserDetails;
import root.demo.repositories.ESRepository;
import root.demo.repositories.PaperRepository;

@Service
public class Indexer {

	@Autowired
	private ESRepository repository;

	@Autowired
	private PaperRepository paperRepository;

	public Indexer() {
	}

	public boolean delete(String filename) {
		if (repository.equals(filename)) {
			repository.deleteById(filename);
			return true;
		} else
			return false;

	}

	public boolean update(IndexUnit unit) {
		unit = repository.save(unit);
		if (unit != null)
			return true;
		else
			return false;
	}

	public boolean add(IndexUnit unit) {
		unit = repository.index(unit);
		if (unit != null)
			return true;
		else
			return false;
	}

	/**
	 * 
	 * @param file Direktorijum u kojem se nalaze dokumenti koje treba indeksirati
	 */
	public int index(File file) {
		DocumentHandler handler = null;
		String fileName = null;
		int retVal = 0;
		try {
			File[] files;
			if (file.isDirectory()) {
				files = file.listFiles();
			} else {
				files = new File[1];
				files[0] = file;
			}
			for (File newFile : files) {
				if (newFile.isFile()) {
					fileName = newFile.getName();
					Paper p = paperRepository.findByFile(fileName);
					handler = getHandler(fileName);
					if (handler == null) {
						System.out.println("Nije moguce indeksirati dokument sa nazivom: " + fileName);
						continue;
					}
					String text = handler.getText(newFile);
					ScientificArea sc = p.getScArea();
					Magazine m = p.getMagazine();
					User author = p.getAuthor();
					UserDetails authorDetails = author.getUserDetails();
					IndexUnit iu = new IndexUnit(m.getName(), text, p.getTitle(),
							authorDetails.getFirstName() + " " + authorDetails.getLastName(), p.getKeywords(),
							p.getPaperAbstract(), sc.getName(), p.getFile());
					if (add(iu))
						retVal++;
				} else if (newFile.isDirectory()) {
					retVal += index(newFile);
				}
			}
			System.out.println("indexing done");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("indexing NOT done");
		}
		return retVal;
	}

	public DocumentHandler getHandler(String fileName) {
		if (fileName.endsWith(".txt")) {
			return new TextDocHandler();
		} else if (fileName.endsWith(".pdf")) {
			return new PDFHandler();
		} else if (fileName.endsWith(".doc")) {
			return new WordHandler();
		} else if (fileName.endsWith(".docx")) {
			return new Word2007Handler();
		} else {
			return null;
		}
	}

}