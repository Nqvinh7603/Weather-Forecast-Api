package com.skyapi.weatherforecast.realtime;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.skyapi.weatherforecast.common.RealtimeWeather;

public interface RealtimeWeatherRepository extends CrudRepository<RealtimeWeather, String>{
	@Query("SELECT r from RealtimeWeather r where r.location.countryCode = ?1 and r.location.cityName= ?2")
	public RealtimeWeather findByCountryCodeAndCity(String countryCode, String city);
	
	@Query("SELECT r from RealtimeWeather r where r.id=?1 and r.location.trashed=false")
	public RealtimeWeather findByLocationCode(String locationCode);
}
