package root.demo.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import root.demo.model.es.AdvancedQuery;
import root.demo.model.es.RequiredHighlight;
import root.demo.model.es.ResultData;
import root.demo.model.es.SearchType;
import root.demo.model.es.SimpleQuery;
import root.demo.services.LocationService;
import root.demo.services.es.QueryBuilder;
import root.demo.services.es.ResultRetriever;

@RestController
@RequestMapping("/search")
public class SearchController {

	@Autowired
	ResultRetriever resultRetriever;
	
	@Autowired
	LocationService locationService;

	@PostMapping(consumes = "application/json")
	public ResponseEntity<List<ResultData>> searchPhrase(@RequestBody SimpleQuery simpleQuery) throws Exception {
		org.elasticsearch.index.query.QueryBuilder query = QueryBuilder.buildQuery(SearchType.phrase,
				simpleQuery.getField(), simpleQuery.getValue());
		List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();
		rh.add(new RequiredHighlight(simpleQuery.getField(), simpleQuery.getValue()));
		List<ResultData> results = resultRetriever.getResults(query, rh);
		return new ResponseEntity<List<ResultData>>(results, HttpStatus.OK);
	}

	@PostMapping(value = "/boolean", consumes = "application/json")
	public ResponseEntity<List<ResultData>> searchBoolean(@RequestBody AdvancedQuery advancedQuery) throws Exception {
		BoolQueryBuilder builder = QueryBuilders.boolQuery();
		List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();
		System.out.println(advancedQuery.getOperation());
		for (SimpleQuery sq : advancedQuery.getQueries()) {
			org.elasticsearch.index.query.QueryBuilder query = QueryBuilder.buildQuery(SearchType.phrase, sq.getField(),
					sq.getValue());
			if (advancedQuery.getOperation().equalsIgnoreCase("AND")) {
				builder.must(query);
			} else if (advancedQuery.getOperation().equalsIgnoreCase("OR")) {
				builder.should(query);
			}
			rh.add(new RequiredHighlight(sq.getField(), sq.getValue()));
		}
		List<ResultData> results = resultRetriever.getResults(builder, rh);
		return new ResponseEntity<List<ResultData>>(results, HttpStatus.OK);
	}

	@GetMapping(value = "/test")
	public void search() throws RestClientException, URISyntaxException, IOException {
		/*
		 * SimpleQuery sq = new SimpleQuery(); sq.setField("text"); sq.
		 * setValue("Provedite nezaboravan odmor na jednoj od najlepših planina – Kopaoniku!"
		 * ); resultRetriever.getMoreLikeThisReviewers(sq);
		 */
		/*
		 * String city = "Novi Sad"; String country = "Serbia"; Location location =
		 * locationService.getCoordinates(city, country);
		 */
		
		resultRetriever.geoquery();
		

	}

}
