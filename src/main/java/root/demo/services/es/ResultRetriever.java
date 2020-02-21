package root.demo.services.es;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import root.demo.handlers.DocumentHandler;
import root.demo.handlers.PDFHandler;
import root.demo.handlers.TextDocHandler;
import root.demo.handlers.Word2007Handler;
import root.demo.handlers.WordHandler;
import root.demo.model.Magazine;
import root.demo.model.Paper;
import root.demo.model.Review;
import root.demo.model.enums.MagazineType;
import root.demo.model.es.IndexUnit;
import root.demo.model.es.LocationIndex;
import root.demo.model.es.RequiredHighlight;
import root.demo.model.es.ResultData;
import root.demo.model.es.SimpleQuery;
import root.demo.model.users.User;
import root.demo.model.users.UserDetails;
import root.demo.repositories.ESRepository;
import root.demo.repositories.LocationESRepository;
import root.demo.repositories.PaperRepository;
import root.demo.services.json.Conversion;
import root.demo.services.json.Hit;

@Service
public class ResultRetriever {

	@Autowired
	private ESRepository repository;

	@Autowired
	private PaperRepository paperRepository;

	@Autowired
	private ElasticsearchTemplate elasticSearchTemplate;

	@Autowired
	LocationESRepository locationESRepository;

	public ResultRetriever() {
	}

	/*
	 * public List<ResultData> getResults(org.elasticsearch.index.query.QueryBuilder
	 * query, List<RequiredHighlight> requiredHighlights) { if (query == null) {
	 * return null; }
	 * 
	 * List<ResultData> results = new ArrayList<ResultData>();
	 * 
	 * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	 * searchSourceBuilder.query(query); searchSourceBuilder.size(maxHits);
	 * 
	 * HighlightBuilder highlightBuilder = new HighlightBuilder();
	 * highlightBuilder.field("idRada"); highlightBuilder.field("statusRada");
	 * highlightBuilder.field("tipCasopisa");
	 * highlightBuilder.field("nazivCasopisa"); highlightBuilder.field("naslov");
	 * highlightBuilder.field("autor"); highlightBuilder.field("kljucniPojmovi");
	 * highlightBuilder.field("text"); highlightBuilder.field("naucnaOblast");
	 * highlightBuilder.fragmentSize(100);
	 * highlightBuilder.preTags("<spam style='color:red'><b>").postTags(
	 * "</b></spam>"); highlightBuilder.fragmentSize(500);
	 * searchSourceBuilder.highlighter(highlightBuilder);
	 * 
	 * Search search = new
	 * Search.Builder(searchSourceBuilder.toString()).addIndex("digitallibrary").
	 * addType("book") .build();
	 * 
	 * SearchResult result; try { result = client.execute(search);
	 * List<SearchResult.Hit<IndexUnit, Void>> hits =
	 * result.getHits(IndexUnit.class); ResultData rd;
	 * 
	 * for (SearchResult.Hit<IndexUnit, Void> sd : hits) { String highlight = "";
	 * for (String hf : sd.highlight.keySet()) { for (RequiredHighlight rh :
	 * requiredHighlights) { if (hf.equals(rh.getFieldName())) { highlight +=
	 * sd.highlight.get(hf).toString(); } } }
	 * 
	 * rd = new ResultData(sd.source.getIdRada(), sd.source.getStatusRada(),
	 * sd.source.getTipCasopisa(), sd.source.getNazivCasopisa(),
	 * sd.source.getNaslov(), sd.source.getAutor(), sd.source.getKljucniPojmovi(),
	 * sd.source.getText(), sd.source.getNaucnaOblast(), sd.source.getRecenzenti(),
	 * sd.source.getFilename(), highlight); results.add(rd); } } catch (IOException
	 * e) { e.printStackTrace(); }
	 * 
	 * return results; }
	 */

	public List<ResultData> getResults(org.elasticsearch.index.query.QueryBuilder query,
			List<RequiredHighlight> requiredHighlights) {
		if (query == null) {
			return null;
		}

		List<ResultData> results = new ArrayList<ResultData>();
		for (IndexUnit indexUnit : repository.search(query)) {
			Paper p = paperRepository.findByFile(indexUnit.getFile());
			Magazine m = p.getMagazine();
			User author = p.getAuthor();
			UserDetails authorDetails = author.getUserDetails();
			boolean free = false;
			if (m.getType().equals(MagazineType.openAccess)) {
				results.add(new ResultData(indexUnit.getTitle(), p.getDoi(),
						authorDetails.getFirstName() + " " + authorDetails.getLastName(), indexUnit.getFile(),
						m.getName(), free));
			}

		}
		// TODO ako stignes drkaj se s tim highliterima, ima kod bojane, ako ne, ko ga
		// jebe
		return results;
	}

	public Set<User> getMoreLikeThisReviewers(SimpleQuery simpleQuery) {
		List<ResultData> results = new ArrayList<ResultData>();
		String[] fields = { simpleQuery.getField().toString() };
		String[] likeTexts = { simpleQuery.getValue().toString() };
		MoreLikeThisQueryBuilder.Item[] items = { new MoreLikeThisQueryBuilder.Item("library", "", "file") };
		MoreLikeThisQueryBuilder qb = QueryBuilders.moreLikeThisQuery(fields, likeTexts, items);
		qb.minDocFreq(1);
		qb.minTermFreq(1);
		qb.maxQueryTerms(100);
		System.out.println("\nmore like this qb:\n" + qb);

		SearchResponse response = elasticSearchTemplate.getClient().prepareSearch("library").setQuery(qb).get();

		for (SearchHit o : response.getHits()) {
			ObjectMapper objectMapper = new ObjectMapper();
			ResultData result = null;

			try {
				result = objectMapper.readValue(o.getSourceAsString(), ResultData.class);
				results.add(result);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		List<User> reviewers = new ArrayList<>();

		for (ResultData rd : results) {
			Paper p = paperRepository.findByFile(rd.getFile());
			for (Review r : p.getReviews()) {
				reviewers.add(r.getReviewer());
			}
		}

		Set<User> foundReviewers = new HashSet<>(reviewers);
		return foundReviewers;
	}

	public IndexUnit findIndexUnit(String file) {
		return repository.findByFile(file);
	}

	public void geoquery() throws IOException { // TODO dobijas ovde listu reviewera, autora i listu koautora i odradis
												// njihov
		// size,
		// da znas koliko treba da picis lokacija, tj samo size od autora i liste
		// koautra, s tim se uporedjuje
		LocationIndex author = new LocationIndex("author", new GeoPoint(45.2671, 19.8335)); // ns
		LocationIndex coauthor1 = new LocationIndex("coauthor1", new GeoPoint(44.7866, 20.4489)); // bg
		LocationIndex coauthor2 = new LocationIndex("coauthor2", new GeoPoint(43.8556, 19.8425)); // uzice
		LocationIndex reviewer1 = new LocationIndex("reviewer1", new GeoPoint(44.7489, 19.6908)); // sabac
		LocationIndex reviewer2 = new LocationIndex("reviewer2", new GeoPoint(42.6977, 23.3219)); // sofia
		LocationIndex reviewer3 = new LocationIndex("reviewer3", new GeoPoint(44.8740, 20.6476)); // pancevo
		LocationIndex reviewer4 = new LocationIndex("reviewer4", new GeoPoint(47.4979, 19.0402)); // budimpesta

		locationESRepository.index(author);
		locationESRepository.index(coauthor1);
		locationESRepository.index(coauthor2);
		locationESRepository.index(reviewer1);
		locationESRepository.index(reviewer2);
		locationESRepository.index(reviewer3);
		locationESRepository.index(reviewer4);

		JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
		ObjectNode pushContent = nodeFactory.objectNode();
		ObjectNode query = nodeFactory.objectNode();
		ObjectNode bool = nodeFactory.objectNode();

		ArrayNode must_not = nodeFactory.arrayNode();

		List<LocationIndex> list = new ArrayList<>();
		list.add(author);
		list.add(coauthor1);
		list.add(coauthor2);
		for (LocationIndex index : list) {
			ObjectNode mustNotObj = nodeFactory.objectNode();
			ObjectNode geo_distance = nodeFactory.objectNode();
			geo_distance.put("distance", "100km");
			geo_distance.put("location", Double.toString(index.getLocation().getLat()) + ","
					+ Double.toString(index.getLocation().getLon()));
			mustNotObj.set("geo_distance", geo_distance);
			must_not.add(mustNotObj);
		}

		bool.set("must_not", must_not);
		query.set("bool", bool);
		pushContent.set("query", query);
		System.out.println(pushContent.toString());

		/*
		 * HttpHeaders headers = new HttpHeaders();
		 * headers.setContentType(MediaType.APPLICATION_JSON);
		 * headers.setContentLength(pushContent.toString().getBytes().length);
		 * List<MediaType> mt = new ArrayList<>(); mt.add(MediaType.ALL);
		 * headers.setAccept(mt); HttpEntity<String> entity = new
		 * HttpEntity<>(pushContent.toString(), headers); // TODO headeri ne valjaju
		 * nesto, // ne dobije se // dobar odgovor preko ovoga a preko postmana da
		 * ResponseEntity<String> result =
		 * template.exchange("http://localhost:9200/location/_search", HttpMethod.GET,
		 * entity, String.class, new HashMap<>());
		 * 
		 * System.out.println(result.getBody());
		 */

		URL url = new URL("http://localhost:9200/location/_search");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json; utf-8");
		con.setRequestProperty("Accept", "application/json");
		con.setDoOutput(true);
		String jsonInputString = pushContent.toString();
		try (OutputStream os = con.getOutputStream()) {
			byte[] input = jsonInputString.getBytes("utf-8");
			os.write(input, 0, input.length);
		}

		ObjectMapper mapper = new ObjectMapper();
		String responseFromES = "";
		try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());

			}
			System.out.println(response.toString());
			responseFromES = response.toString();

		}

		Conversion conversion = mapper.readValue(responseFromES, Conversion.class);

		List<Hit> hits = conversion.getHits().getHits();
		List<String> reviewerName = new ArrayList<>();
		for (Hit hit : hits) {
			reviewerName.add(hit.getId());
		}

		for (String name : reviewerName) {
			System.out.println(name);
		}

	}

	protected DocumentHandler getHandler(String fileName) {
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
