package com.skyapi.weatherforecast.daily;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.CommonUtility;
import com.skyapi.weatherforecast.GeolocationService;
import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.Location;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
@RestController
@RequestMapping("/v1/daily")
public class DailyWeatherApiController {
	private DailyWeatherService dailyWeatherService;
	private GeolocationService locationService;
	private ModelMapper modelMapper;
	public DailyWeatherApiController(DailyWeatherService dailyWeatherService, GeolocationService locationService,
			ModelMapper modelMapper) {
		super();
		this.dailyWeatherService = dailyWeatherService;
		this.locationService = locationService;
		this.modelMapper = modelMapper;
	}
	
	@GetMapping
	public ResponseEntity<?> listDailyForecastByIPAddress(HttpServletRequest request) throws Exception{
		String ipAddress = CommonUtility.getIPAddress(request);
		
		Location locationFromIP = locationService.getLocation(ipAddress);
		
		List<DailyWeather> dailyForecast = dailyWeatherService.getByLocation(locationFromIP);
		if(dailyForecast.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(listEntity2DTO(dailyForecast));
		
	}
	
	private DailyWeatherListDTO listEntity2DTO(List<DailyWeather> dailyForecast) {
		Location location = dailyForecast.get(0).getId().getLocation();
		DailyWeatherListDTO listDTO = new DailyWeatherListDTO();
		listDTO.setLocation(location.toString());
		dailyForecast.forEach(dailyWeather -> {
			listDTO.addDailyWeatherDTO(modelMapper.map(dailyWeather, DailyWeatherDTO.class));
		});
		return listDTO;
	}
	
	@GetMapping("/{locationCode}")
	public ResponseEntity<?> listHourlyForecastByLocationCode(@PathVariable("locationCode") String locationCode){
		List<DailyWeather> dailyForecast = dailyWeatherService.getByLocationCode(locationCode);
		if(dailyForecast.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(listEntity2DTO(dailyForecast));
	}
}
