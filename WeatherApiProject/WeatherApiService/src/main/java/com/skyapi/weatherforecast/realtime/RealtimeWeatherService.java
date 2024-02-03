package com.skyapi.weatherforecast.realtime;

import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.location.LocationRepository;
import java.util.*;
@Service
public class RealtimeWeatherService {
	private RealtimeWeatherRepository realtimeWeatherRepository;
	private LocationRepository locationRepo;
	

	public RealtimeWeatherService(RealtimeWeatherRepository realtimeWeatherRepository,
			LocationRepository locationRepo) {
		super();
		this.realtimeWeatherRepository = realtimeWeatherRepository;
		this.locationRepo = locationRepo;
	}
	public RealtimeWeather getByLocation(Location location) throws LocationNotFoundException {
		String countryCode = location.getCountryCode();
		String cityName = location.getCityName();
		RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByCountryCodeAndCity(countryCode,
				cityName);
		if(realtimeWeather == null) {
			throw new LocationNotFoundException("No location found with the given country code and city name");
		}
		return realtimeWeather;
	}
	public RealtimeWeather getByLocationCode(String locationCode) throws LocationNotFoundException {
		RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByLocationCode(locationCode);
		if(realtimeWeather == null) {
			throw new LocationNotFoundException("No location found with the given code: " + locationCode);
		}
		return realtimeWeather;
	}
	public RealtimeWeather update(String locationCode, RealtimeWeather realtimeWeather) throws LocationNotFoundException {
		Location location = locationRepo.findByCode(locationCode);
		if(location == null) {
			throw new LocationNotFoundException("No location found with the given code: " + locationCode);
		}
		
		realtimeWeather.setLocation(location);
		realtimeWeather.setLastUpdated(new Date());
		
		if (location.getRealtimeWeather() == null ) {
			location.setRealtimeWeather(realtimeWeather);
			return (locationRepo.save(location)).getRealtimeWeather();
		}
		
		return realtimeWeatherRepository.save(realtimeWeather);
	}
}
