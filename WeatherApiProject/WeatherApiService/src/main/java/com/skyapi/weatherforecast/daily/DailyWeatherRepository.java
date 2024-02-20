package com.skyapi.weatherforecast.daily;

import org.springframework.data.repository.CrudRepository;

import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.DailyWeatherId;

public interface DailyWeatherRepository extends CrudRepository<DailyWeather, DailyWeatherId> {

}
