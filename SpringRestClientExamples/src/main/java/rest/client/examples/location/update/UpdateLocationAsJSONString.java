package rest.client.examples.location.update;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import rest.client.examples.location.Location;

public class UpdateLocationAsJSONString {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String requestURI = "http://localhost:8080/v1/locations";
		RestTemplate restTemplate = new RestTemplate();

		Location location = new Location();
		location.setCode("DANA_VN");
		location.setCityName("Da Nang");
		location.setRegionName("Da Nang");
		location.setCountryCode("VN");
		location.setCountryName("Vietnam");
		location.setEnabled(false);
		
		HttpEntity<Location> request = new HttpEntity<>(location);
		
		ResponseEntity<Location> response= restTemplate.exchange(requestURI, HttpMethod.PUT, request, Location.class);
		
		if(response.getStatusCode().is2xxSuccessful()) {
			Location updatedLocation = response.getBody();
			System.out.println(updatedLocation);
		}
	}

}
