package com.skyapi.weatherforecast;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.hourly.HourlyWeatherDTO;

@SpringBootApplication
public class WeatherApiServiceApplication {
	@Bean
	public ModelMapper getModelMapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		var typeMap =mapper.typeMap(HourlyWeather.class, HourlyWeatherDTO.class);
		typeMap.addMapping(src -> src.getId().getHourOfDay(), HourlyWeatherDTO::setHourOfDay);
		return mapper;
	}
	public static void main(String[] args) {
		SpringApplication.run(WeatherApiServiceApplication.class, args);
	}

}
