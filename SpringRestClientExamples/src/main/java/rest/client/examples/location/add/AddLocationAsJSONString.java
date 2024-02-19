package rest.client.examples.location.add;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AddLocationAsJSONString {

	public static void main(String[] args) {
		String requestURI = "http://localhost:8080/v1/locations";
		RestTemplate restTemplate = new RestTemplate();
		String json = """
								{
				  "code": "MADRID_ES",
				  "city_name": "Madrid",
				  "region_name": "Community of Madrid",
				  "country_code": "US",
				  "country_name": "Spain",
				  "enabled": true
				}
								""";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(json, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(requestURI, request, String.class);
		HttpStatusCode statusCode = response.getStatusCode();
		System.out.println("Response status code: " + statusCode);
		String addedLocationJSON = response.getBody();
		System.out.println(addedLocationJSON);
	}

}
