package rest.client.examples.location.get;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;



public class GetLocationAsObject {

	public static void main(String[] args) {
		String requestURI = "http://localhost:8080/v1/locations/{code}";
		Map<String, String> params = new HashMap<>();
		params.put("code", "DANA_VN");
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<String> response = restTemplate.getForEntity(requestURI, String.class, params);
		
		HttpStatusCode statusCode = response.getStatusCode();
		
		System.out.println("Response status code: " + statusCode);
		
		if (statusCode.value() == HttpStatus.OK.value()) {
			String location = response.getBody();
			System.out.println(location);
		}
	}

}