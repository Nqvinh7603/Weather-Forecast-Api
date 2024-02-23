package com.skyapi.weatherforecast.daily;


import java.util.ArrayList;
import java.util.List;
public class DailyWeatherListDTO {

	private String location;
	
	private List<DailyWeatherDTO> dailyForecast = new ArrayList<>();

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<DailyWeatherDTO> getDailyForecast() {
		return dailyForecast;
	}

	public void setDailyForecast(List<DailyWeatherDTO> dailyForecast) {
		this.dailyForecast = dailyForecast;
	}
	
	public void addDailyWeatherDTO(DailyWeatherDTO dto) {
		this.dailyForecast.add(dto);
	}
	
}
