package com.skyapi.weatherforecast.realtime;

import org.springframework.data.repository.CrudRepository;

import com.skyapi.weatherforecast.common.RealtimeWeather;

public interface RealtimeWeatherRepository extends CrudRepository<RealtimeWeather, String>{

}
