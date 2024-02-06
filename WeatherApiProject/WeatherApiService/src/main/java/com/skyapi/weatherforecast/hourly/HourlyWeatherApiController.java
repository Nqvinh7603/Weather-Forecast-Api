package com.skyapi.weatherforecast.hourly;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.CommonUtility;
import com.skyapi.weatherforecast.GeolocationException;
import com.skyapi.weatherforecast.GeolocationService;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.location.LocationService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/hourly")
public class HourlyWeatherApiController {
	private HourlyWeatherService hourlyWeatherService;
	private GeolocationService geolocationService;
	public HourlyWeatherApiController(HourlyWeatherService hourlyWeatherService,
			GeolocationService geolocationService) {
		super();
		this.hourlyWeatherService = hourlyWeatherService;
		this.geolocationService = geolocationService;
	}
	@GetMapping
	public ResponseEntity<?> listHourlyForecastByIPAddress(HttpServletRequest request) throws Exception{
		String ipAddress = CommonUtility.getIPAddress(request);
		int currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));
		try {
		Location location = geolocationService.getLocation(ipAddress);
		List<HourlyWeather> hourlyForecast=hourlyWeatherService.getByLocation(location, currentHour);
		if(hourlyForecast.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(hourlyForecast); 
		}catch(GeolocationException ex) {
			return ResponseEntity.badRequest().build();
		}catch(LocationNotFoundException ex) {
			return ResponseEntity.notFound().build();
		} 
	}
}
