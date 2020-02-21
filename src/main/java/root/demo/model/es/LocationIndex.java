package root.demo.model.es;

import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

@Document(indexName = "location", shards = 1, replicas = 0)
public class LocationIndex {

	public static final String DATE_PATTERN = "yyyy-MM-dd";
	
	@Id
	@Field(type = FieldType.Text, store = true)
	private String name; // author/coauthor/reviewer first name
	@GeoPointField
	private GeoPoint location;

	public LocationIndex() {

	}

	public LocationIndex(String name, GeoPoint location) {
		super();
		this.name = name;
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GeoPoint getLocation() {
		return location;
	}

	public void setLocation(GeoPoint location) {
		this.location = location;
	}

}
