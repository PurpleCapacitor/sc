package root.demo.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PaperDTO implements Serializable {

	private String title;
	private String keywords;
	private String paperAbstract;
	private int noCoAuthors;
	private String scientificArea;
	private String author;
	private String magazine;
	private String file;

	public PaperDTO() {

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

	public int getNoCoAuthors() {
		return noCoAuthors;
	}

	public void setNoCoAuthors(int noCoAuthors) {
		this.noCoAuthors = noCoAuthors;
	}

	public String getScientificArea() {
		return scientificArea;
	}

	public void setScientificArea(String scientificArea) {
		this.scientificArea = scientificArea;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getMagazine() {
		return magazine;
	}

	public void setMagazine(String magazine) {
		this.magazine = magazine;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

}
