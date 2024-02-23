package com.skyapi.weatherforecast.daily;

import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.CommonUtility;
import com.skyapi.weatherforecast.GeolocationService;
import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.Location;

import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
@RestController
@RequestMapping("/v1/daily")
@Validated
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
	@PutMapping("/{locationCode}")
	public ResponseEntity<?> updateDailyForecast(@PathVariable("locationCode") String code,
			@RequestBody @Valid List<DailyWeatherDTO> listDTO) throws BadRequestException {
		
		if (listDTO.isEmpty()) {
			throw new BadRequestException("Daily forecast data cannot be empty");
		}
		
		listDTO.forEach(System.out::println);
		
		List<DailyWeather> dailyWeather = listDTO2ListEntity(listDTO);
		
		System.out.println("================");
		
		dailyWeather.forEach(System.out::println);		
		
		List<DailyWeather> updatedForecast = dailyWeatherService.updateByLocationCode(code, dailyWeather);
		
		
		
		return ResponseEntity.ok(listEntity2DTO(updatedForecast));
	}	
	private List<DailyWeather> listDTO2ListEntity(List<DailyWeatherDTO> listDTO) {
		List<DailyWeather> listEntity = new ArrayList<>();
		
		listDTO.forEach(dto -> {
			listEntity.add(modelMapper.map(dto, DailyWeather.class));
		});
		
		return listEntity;
	}	
	
}
