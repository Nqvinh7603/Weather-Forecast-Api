package rest.client.examples.location.get;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import rest.client.examples.location.Location;


public class GetLocationAsString {

	public static void main(String[] args) {
		String requestURI = "http://localhost:8080/v1/locations/DELHI_IN";
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<Location> response = restTemplate.getForEntity(requestURI, Location.class);
		
		HttpStatusCode statusCode = response.getStatusCode();
		
		System.out.println("Response status code: " + statusCode);
		
		if (statusCode.value() == HttpStatus.OK.value()) {
			Location location = response.getBody();
			System.out.println(location);
		}
	}

}