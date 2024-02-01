package com.skyapi.weatherforecast.realtime;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.CommonUtility;
import com.skyapi.weatherforecast.GeolocationException;
import com.skyapi.weatherforecast.GeolocationService;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.location.LocationNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/realtime")
public class RealtimeWeatherApiController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RealtimeWeatherApiController.class);
	private GeolocationService locationService;
	private RealtimeWeatherService realtimeWeatherService;
	private ModelMapper modelMapper;
	
	
	public RealtimeWeatherApiController(GeolocationService locationService,
			RealtimeWeatherService realtimeWeatherService, ModelMapper modelMapper) {
		super();
		this.locationService = locationService;
		this.realtimeWeatherService = realtimeWeatherService;
		this.modelMapper = modelMapper;
	}


	@GetMapping
	public ResponseEntity<?> getRealtimeWeatherByIPAddress(HttpServletRequest request) throws Exception {
		String ipAddress = CommonUtility.getIPAddress(request);
		try {
			Location locationFromIP =  locationService.getLocation(ipAddress);
			RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocation(locationFromIP);
			RealtimeWeatherDTO dto = modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);
			return ResponseEntity.ok(dto);
		} catch (GeolocationException e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseEntity.badRequest().build();
			
		} catch (LocationNotFoundException e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/{locationCode}")
	public ResponseEntity<?> getRealtimeWeatherByLocationCode(@PathVariable("locationCode") String locationCode){
		try {
			RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocationCode(locationCode);
			RealtimeWeatherDTO dto = modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);
			return ResponseEntity.ok(dto);
		} catch (LocationNotFoundException e) {
			// TODO: handle exception
			LOGGER.error(e.getMessage(), e);
			return ResponseEntity.notFound().build();
		}
	}
}
