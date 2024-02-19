package rest.client.examples.location.add;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import rest.client.examples.location.Location;

public class AddLocationAsObject {

	public static void main(String[] args) {
		String requestURI = "http://localhost:8080/v1/locations";
		RestTemplate restTemplate = new RestTemplate();
		Location location = new Location();
		location.setCode("BNGL_IN");
		location.setCityName("Bangalore");
		location.setRegionName("Karnataka");
		location.setCountryCode("IN");
		location.setCountryName("India");
		
		HttpEntity<Location> request = new HttpEntity<>(location);
		ResponseEntity<Location> response = restTemplate.postForEntity(requestURI, request, Location.class);
		HttpStatusCode statusCode = response.getStatusCode();
		System.out.println("Response status code: " + statusCode);
		Location addedLocation = response.getBody();
		System.out.println(addedLocation);
	}

}
