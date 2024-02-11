package com.skyapi.weatherforecast.hourly;

import java.util.ArrayList;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.CommonUtility;
import com.skyapi.weatherforecast.GeolocationException;
import com.skyapi.weatherforecast.GeolocationService;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/v1/hourly")
public class HourlyWeatherApiController {
	private HourlyWeatherService hourlyWeatherService;
	private GeolocationService geolocationService;
	private ModelMapper modelMapper;

	public HourlyWeatherApiController(HourlyWeatherService hourlyWeatherService, GeolocationService geolocationService,
			ModelMapper modelMapper) {
		super();
		this.hourlyWeatherService = hourlyWeatherService;
		this.geolocationService = geolocationService;
		this.modelMapper = modelMapper;
	}

	@GetMapping
	public ResponseEntity<?> listHourlyForecastByIPAddress(HttpServletRequest request) throws Exception {
		String ipAddress = CommonUtility.getIPAddress(request);

		try {
			int currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));
			Location location = geolocationService.getLocation(ipAddress);
			List<HourlyWeather> hourlyForecast = hourlyWeatherService.getByLocation(location, currentHour);
			if (hourlyForecast.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.ok(listEntity2DTO(hourlyForecast));
		} catch (NumberFormatException | GeolocationException ex) {
			return ResponseEntity.badRequest().build();
		} catch (LocationNotFoundException ex) {
			return ResponseEntity.notFound().build();
		}
	}

	private HourlyWeatherListDTO listEntity2DTO(List<HourlyWeather> hourlyForecast) {
		Location location = hourlyForecast.get(0).getId().getLocation();

		HourlyWeatherListDTO listDTO = new HourlyWeatherListDTO();
		listDTO.setLocation(location.toString());

		hourlyForecast.forEach(hourlyWeather -> {
			HourlyWeatherDTO dto = modelMapper.map(hourlyWeather, HourlyWeatherDTO.class);
			listDTO.addWeatherHourlyDTO(dto);
		});

		return listDTO;
	}
	@GetMapping("/{locationCode}")
	public ResponseEntity<?> listHourlyForecastByLocationCode(@PathVariable("locationCode") String locationCode, HttpServletRequest request) {
		try {
			int currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));
			List<HourlyWeather> hourlyForecast = hourlyWeatherService.getByLocationCode(locationCode, currentHour);
			if(hourlyForecast.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.ok(listEntity2DTO(hourlyForecast));
		} catch (NumberFormatException e) {
			return ResponseEntity.badRequest().build();
		}catch (LocationNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PutMapping("/{locationCode}")
	public ResponseEntity<?> updateHourlyForecast(@PathVariable("locationCode") String locationCode, @RequestBody List<HourlyWeatherDTO> listDTO) throws com.skyapi.weatherforecast.BadRequestException {
		if(listDTO.isEmpty()) {
			throw new com.skyapi.weatherforecast.BadRequestException("Hourly forecast data cannot be empty");
		}
		listDTO.forEach(System.out::println);
		List<HourlyWeather> listHourlyWeather = listDTO2ListEntity(listDTO);
		System.out.println();
		listHourlyWeather.forEach(System.out::println);
		return ResponseEntity.accepted().build();
	}
	private List<HourlyWeather> listDTO2ListEntity(List<HourlyWeatherDTO> listDTO){
		List<HourlyWeather> listEntity = new ArrayList<>();
		listDTO.forEach(dto -> {
			listEntity.add(modelMapper.map(dto, HourlyWeather.class));
		});
		return listEntity;
	}
}
