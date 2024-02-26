package com.skyapi.weatherforecast.daily;

import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.location.LocationRepository;

import java.util.ArrayList;
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
	
	public List<DailyWeather> getByLocationCode(String locationCode){
		Location location = locationRepository.findByCode(locationCode);
		if(location == null) {
			throw new LocationNotFoundException(locationCode);
		}
		return dailyWeatherRepository.findByLocationCode(locationCode);
	}
	public List<DailyWeather> updateByLocationCode(String code, List<DailyWeather> dailyWeatherInRequest) 
			throws LocationNotFoundException {
		Location location = locationRepository.findByCode(code);
		
		if (location == null) {
			throw new LocationNotFoundException(code);
		}
		
		for (DailyWeather data : dailyWeatherInRequest) {
			data.getId().setLocation(location);
		}
		
		List<DailyWeather> dailyWeatherInDB = location.getListDailyWeather();
		List<DailyWeather> dailyWeatherToBeRemoved = new ArrayList<>();
		
		for (DailyWeather forecast : dailyWeatherInDB) {
			if (!dailyWeatherInRequest.contains(forecast)) {
				dailyWeatherToBeRemoved.add(forecast.getShallowCopy());
			}
		}
		
		for (DailyWeather forecastToBeRemoved : dailyWeatherToBeRemoved) {
			dailyWeatherInDB.remove(forecastToBeRemoved);
		}
		
		return (List<DailyWeather>) dailyWeatherRepository.saveAll(dailyWeatherInRequest);
	}	
}
