package com.skyapi.weatherforecast.full;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.CommonUtility;
import com.skyapi.weatherforecast.GeolocationService;
import com.skyapi.weatherforecast.common.Location;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/full")
public class FullWeatherApiController {
	private GeolocationService locationService;
	private FullWeatherService weatherService;
	private ModelMapper modelMapper;
	
	
	public FullWeatherApiController(GeolocationService locationService, FullWeatherService weatherService,
			ModelMapper modelMapper) {
		super();
		this.locationService = locationService;
		this.weatherService = weatherService;
		this.modelMapper = modelMapper;
	}

	@GetMapping
	public ResponseEntity<?> getFullWeatherByIPAddress(HttpServletRequest request) throws Exception  {
		String ipAddress = CommonUtility.getIPAddress(request);
		
		Location locationFromIP = locationService.getLocation(ipAddress);
		Location locationInDB = weatherService.getByLocation(locationFromIP);
		
		FullWeatherDTO dto = entity2DTO(locationInDB);
		
		return ResponseEntity.ok(dto);
	}
	
	@GetMapping("/{locationCode}")
	public ResponseEntity<?> getFullWeatherByLocationCode(@PathVariable String locationCode) {
		
		Location locationInDB = weatherService.get(locationCode);
		
		FullWeatherDTO dto = entity2DTO(locationInDB);
		
		return ResponseEntity.ok(dto);
	}	
	private FullWeatherDTO entity2DTO(Location entity) {
		FullWeatherDTO dto = modelMapper.map(entity, FullWeatherDTO.class);
		
		dto.getRealtimeWeather().setLocation(null);
		return dto;
	}
	
	private Location dto2Entity(FullWeatherDTO dto) {
		return modelMapper.map(dto, Location.class);
	}
	
}
