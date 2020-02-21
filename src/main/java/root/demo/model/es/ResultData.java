package root.demo.model.es;

public final class ResultData {

	private String title;
	private String doi;
	private String authorName;
	private String file;
	private String magazineName;
	private Boolean free;
	private String text;
	private String keywords;
	private String paperAbstract;
	private String scientificArea;

	public ResultData() {
		super();
	}

	public ResultData(String title, String doi, String authorName, String file, String magazineName, Boolean free) {
		super();
		this.title = title;
		this.doi = doi;
		this.authorName = authorName;
		this.file = file;
		this.magazineName = magazineName;
		this.free = free;
	}

	public ResultData(String title, String doi, String authorName, String file, String magazineName, Boolean free,
			String text) {
		super();
		this.title = title;
		this.doi = doi;
		this.authorName = authorName;
		this.file = file;
		this.magazineName = magazineName;
		this.free = free;
		this.text = text;
	}

	public String getMagazineName() {
		return magazineName;
	}

	public void setMagazineName(String magazineName) {
		this.magazineName = magazineName;
	}

	public String getDoi() {
		return doi;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getScientificArea() {
		return scientificArea;
	}

	public void setScientificArea(String scientificArea) {
		this.scientificArea = scientificArea;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getFree() {
		return free;
	}

	public void setFree(Boolean free) {
		this.free = free;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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

}
