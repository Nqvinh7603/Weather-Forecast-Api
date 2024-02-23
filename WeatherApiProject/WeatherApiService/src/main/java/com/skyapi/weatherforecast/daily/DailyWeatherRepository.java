package com.skyapi.weatherforecast.daily;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.DailyWeatherId;
import java.util.*;

public interface DailyWeatherRepository extends CrudRepository<DailyWeather, DailyWeatherId> {
	@Query("""
				
			""")
	public List<DailyWeather> findByLocationCode(String locationCode);
}
