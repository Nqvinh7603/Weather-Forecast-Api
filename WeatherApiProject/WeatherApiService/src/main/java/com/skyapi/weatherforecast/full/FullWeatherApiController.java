package com.skyapi.weatherforecast.full;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.GeolocationService;

@RestController
@RequestMapping("/v1/full")
public class FullWeatherApiController {
	private GeolocationService locationService;
	private FullWeatherService weatherService;
	public FullWeatherApiController(GeolocationService locationService, FullWeatherService weatherService) {
		super();
		this.locationService = locationService;
		this.weatherService = weatherService;
	}
	
	@GetMapping
	public ResponseEntity<?> getFullWeatherByIPAddress(){
		return null;
	}
	
}
