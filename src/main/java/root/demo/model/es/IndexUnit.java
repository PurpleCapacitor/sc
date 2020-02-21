package root.demo.model.es;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = IndexUnit.INDEX_NAME, /* type = IndexUnit.TYPE_NAME */ shards = 1, replicas = 0)
public class IndexUnit {

	public static final String INDEX_NAME = "library";
	// public static final String TYPE_NAME = "paper";
	public static final String ANALYZER_NAME = "serbian";
	public static final String DATE_PATTERN = "yyyy-MM-dd";

	@Field(type = FieldType.Text, store = true, analyzer = ANALYZER_NAME)
	private String magazineName;
	@Field(type = FieldType.Text, store = true, analyzer = ANALYZER_NAME)
	private String text;
	@Field(type = FieldType.Text, store = true, analyzer = ANALYZER_NAME)
	private String title;
	@Field(type = FieldType.Text, store = true, analyzer = ANALYZER_NAME)
	private String authorName;
	@Field(type = FieldType.Text, store = true, analyzer = ANALYZER_NAME)
	private String keywords;
	@Field(type = FieldType.Text, store = true, analyzer = ANALYZER_NAME)
	private String paperAbstract;
	@Field(type = FieldType.Text, store = true, analyzer = ANALYZER_NAME)
	private String scientificArea;
	@Id
	@Field(type = FieldType.Text, store = true, analyzer = ANALYZER_NAME)
	private String file;

	public IndexUnit() {

	}

	public IndexUnit(String magazineName, String text, String title, String authorName, String keywords,
			String paperAbstract, String scientificArea, String file) {
		super();
		this.magazineName = magazineName;
		this.text = text;
		this.title = title;
		this.authorName = authorName;
		this.keywords = keywords;
		this.paperAbstract = paperAbstract;
		this.scientificArea = scientificArea;
		this.file = file;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getMagazineName() {
		return magazineName;
	}

	public void setMagazineName(String magazineName) {
		this.magazineName = magazineName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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

	@Override
	public String toString() {
		return "IndexUnit [magazineName=" + magazineName + ", text=" + text + ", title=" + title + ", authorName="
				+ authorName + ", keywords=" + keywords + ", paperAbstract=" + paperAbstract + ", scientificArea="
				+ scientificArea + ", file=" + file + "]";
	}

}