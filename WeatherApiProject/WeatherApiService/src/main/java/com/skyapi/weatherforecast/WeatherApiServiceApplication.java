package com.skyapi.weatherforecast;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.daily.DailyWeatherDTO;
import com.skyapi.weatherforecast.full.FullWeatherDTO;
import com.skyapi.weatherforecast.hourly.HourlyWeatherDTO;

@SpringBootApplication
public class WeatherApiServiceApplication {
	@Bean
	public ModelMapper getModelMapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		var typeMap1 =mapper.typeMap(HourlyWeather.class, HourlyWeatherDTO.class);
		typeMap1.addMapping(src -> src.getId().getHourOfDay(), HourlyWeatherDTO::setHourOfDay);
		
		var typeMap2 =mapper.typeMap(HourlyWeatherDTO.class, HourlyWeather.class);
		typeMap2.addMapping(src -> src.getHourOfDay(), (dest,value) -> dest.getId().setHourOfDay(value != null ? (int) value : 0));
				
		var typeMap3 = mapper.typeMap(DailyWeather.class, DailyWeatherDTO.class); 
		typeMap3.addMapping(src -> src.getId().getDayOfMonth(), DailyWeatherDTO::setDayOfMonth);
		typeMap3.addMapping(src -> src.getId().getMonth(), DailyWeatherDTO::setMonth);
		
		var typeMap4 = mapper.typeMap(DailyWeatherDTO.class, DailyWeather.class);
		typeMap4.addMapping(src -> src.getDayOfMonth(), (dest,value) -> dest.getId().setDayOfMonth(value != null ? (int) value : 0));
		typeMap4.addMapping(src -> src.getMonth(), (dest,value) -> dest.getId().setMonth(value != null ? (int) value : 0));
		
		
		var typeMap5 = mapper.typeMap(Location.class, FullWeatherDTO.class);
		typeMap5.addMapping(src -> src.toString(), FullWeatherDTO::setLocation);
		return mapper;
	}
	public static void main(String[] args) {
		SpringApplication.run(WeatherApiServiceApplication.class, args);
	}

}
