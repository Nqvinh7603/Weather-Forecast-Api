package com.skyapi.weatherforecast.full;

import java.util.List;

import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
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
		if (locationInDB == null) {
			throw new LocationNotFoundException(countryCode, cityName);
		}
		return null;
	}

	public Location get(String locationCode) {
		Location location = repo.findByCode(locationCode);
		if (location == null) {
			throw new LocationNotFoundException(locationCode);
		}
		return location;
	}

	public Location update(String locationCode, Location locationInRequest) {
		Location locationInDB = repo.findByCode(locationCode);

		if (locationInDB == null) {
			throw new LocationNotFoundException(locationCode);
		}

		RealtimeWeather realtimeWeather = locationInRequest.getRealtimeWeather();
		realtimeWeather.setLocation(locationInDB);
		
		List<DailyWeather> listDailyWeathers = locationInRequest.getListDailyWeather();
		listDailyWeathers.forEach(dw -> dw.getId().setLocation(locationInDB));
		
		List<HourlyWeather> listHourlyWeathers = locationInRequest.getListHourlyWeathers();
		listHourlyWeathers.forEach(hw -> hw.getId().setLocation(locationInDB));
		
		locationInRequest.setCode(locationInDB.getCode());
		locationInRequest.setCityName(locationInDB.getCityName());
		locationInRequest.setRegionName(locationInDB.getRegionName());
		locationInRequest.setCountryCode(locationInDB.getCountryCode());
		locationInRequest.setCountryName(locationInDB.getCountryName());
		locationInRequest.setEnabled(locationInDB.isEnabled());
		locationInRequest.setTrashed(locationInDB.isTrashed());
		return repo.save(locationInRequest);
	}
}
