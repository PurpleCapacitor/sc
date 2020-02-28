package root.demo.services;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import root.demo.dto.LocationDTO;
import root.demo.model.Location;

@Service
public class LocationService {

	private static String locationAPI = "https://eu1.locationiq.com/v1/search.php?key=";
	private static String token = "57df6460fb5eaf";
	private static RestTemplate template = new RestTemplate();

	public Location getCoordinates(String city, String country) throws RestClientException, URISyntaxException {
		try {
			String uri = locationAPI + token + "&city=" + URLEncoder.encode(city, "UTF-8") + "&country="
					+ URLEncoder.encode(country, "UTF-8") + "&format=json&limit=1";
			ResponseEntity<String> jsonResponse = template.getForEntity(new URI(uri), String.class);
			Location coordinates = extractCoordinatesFromJson(jsonResponse.getBody());

			return coordinates;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

	}

	private Location extractCoordinatesFromJson(String json) {		
		Gson gson = new Gson();
		
		Location coordinates = null;
		LocationDTO[] array = gson.fromJson(json, LocationDTO[].class);
		
		for(LocationDTO l : array) {
			coordinates = new Location(l.getLat(), l.getLon());
		}
		
		return coordinates;
	}

}
