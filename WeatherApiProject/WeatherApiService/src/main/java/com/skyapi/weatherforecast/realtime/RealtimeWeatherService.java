package com.skyapi.weatherforecast.realtime;

import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.location.LocationNotFoundException;

@Service
public class RealtimeWeatherService {
	private RealtimeWeatherRepository realtimeWeatherRepository;

	public RealtimeWeatherService(RealtimeWeatherRepository realtimeWeatherRepository) {
		super();
		this.realtimeWeatherRepository = realtimeWeatherRepository;
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
}
