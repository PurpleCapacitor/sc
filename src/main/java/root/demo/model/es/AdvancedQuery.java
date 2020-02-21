package root.demo.model.es;

import java.util.ArrayList;
import java.util.List;

public class AdvancedQuery {

	private List<SimpleQuery> queries = new ArrayList<SimpleQuery>();
	private String operation;

	public AdvancedQuery() {
		super();
	}

	public List<SimpleQuery> getQueries() {
		return queries;
	}

	public void setQueries(List<SimpleQuery> queries) {
		this.queries = queries;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

}
