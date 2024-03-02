package com.skyapi.weatherforecast.full;

import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.location.LocationRepository;

@Service
public class FullWeatherService {
	private LocationRepository repo;

	public FullWeatherService(LocationRepository repo) {
		super();
		this.repo = repo;
	}
	public Location getByLocation(Location locationFromIP) {
		String cityName = locationFromIP.getCityName();
		String countryCode = locationFromIP.getCountryCode();
		
		Location locationInDB = repo.findByCountryCodeAndCityName(countryCode, cityName);
		if(locationInDB == null) {
			throw new LocationNotFoundException(countryCode, cityName);
		}
		return null;
	}
	
	public Location get(String locationCode) {
		Location location = repo.findByCode(locationCode);
		if(location == null) {
			throw new LocationNotFoundException(locationCode);
		}
		return location;
	}
}
