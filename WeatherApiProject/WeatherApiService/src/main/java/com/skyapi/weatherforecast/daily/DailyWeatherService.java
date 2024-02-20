package com.skyapi.weatherforecast.daily;

import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.location.LocationRepository;
import java.util.List;

import org.springframework.stereotype.Service;
@Service
public class DailyWeatherService {
	private DailyWeatherRepository dailyWeatherRepository;
	private LocationRepository locationRepository;
	public DailyWeatherService(DailyWeatherRepository dailyWeatherRepository, LocationRepository locationRepository) {
		super();
		this.dailyWeatherRepository = dailyWeatherRepository;
		this.locationRepository = locationRepository;
	}
	
	public List<DailyWeather> getByLocation(Location location){
		String countryCode = location.getCountryCode();
		String cityName = location.getCityName();
		
		Location locationInDB = locationRepository.findByCountryCodeAndCityName(countryCode, cityName);
		if(locationInDB == null) {
			throw new LocationNotFoundException(countryCode, cityName);
		}
		return dailyWeatherRepository.findByLocationCode(locationInDB.getCode());
	}
}
