package com.skyapi.weatherforecast;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.daily.DailyWeatherDTO;
import com.skyapi.weatherforecast.full.FullWeatherApiController;
import com.skyapi.weatherforecast.full.FullWeatherDTO;
import com.skyapi.weatherforecast.full.FullWeatherService;
import com.skyapi.weatherforecast.hourly.HourlyWeatherDTO;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.realtime.RealtimeWeatherDTO;
@WebMvcTest(FullWeatherApiController.class)
public class FullWeatherApiControllerTests {
	private static final String END_POINT_PATH = "/v1/full";
	@Autowired private MockMvc mockMvc;	
	@Autowired private ObjectMapper objectMapper;
	
	@MockBean private FullWeatherService weatherService;
	@MockBean private GeolocationService locationService;
	@Test
	public void testGetByIPShouldReturn400BadRequestBecauseGeolocationException() throws Exception {
		GeolocationException ex = new GeolocationException("Geolocation error");
		Mockito.when(locationService.getLocation(Mockito.anyString())).thenThrow(ex);
		
		mockMvc.perform(get(END_POINT_PATH))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0]", is(ex.getMessage())))
				.andDo(print());
	}
	@Test
	public void testGetByIPShouldReturn404NotFound() throws Exception {
		Location location = new Location().code("DELHI_IN");		
		
		when(locationService.getLocation(Mockito.anyString())).thenReturn(location);
		
		LocationNotFoundException ex = new LocationNotFoundException(location.getCode());
		when(weatherService.getByLocation(location)).thenThrow(ex);
		
		mockMvc.perform(get(END_POINT_PATH))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errors[0]", is(ex.getMessage())))
				.andDo(print());
	}	
	
	@Test
	public void testGetByIPShouldReturn200OK() throws Exception {
		Location location = new Location();
		location.setCode("NYC_USA");
		location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryCode("US");
		location.setCountryName("United States of America");
		
		RealtimeWeather realtimeWeather = new RealtimeWeather();
		realtimeWeather.setTemperature(12);
		realtimeWeather.setHumidity(32);
		realtimeWeather.setLastUpdated(new Date());
		realtimeWeather.setPrecipitation(88);
		realtimeWeather.setStatus("Cloudy");
		realtimeWeather.setWindSpeed(5);
		
		location.setRealtimeWeather(realtimeWeather);
		
		DailyWeather dailyForecast1 = new DailyWeather()
				.location(location)
				.dayOfMonth(16)
				.month(7)
				.minTemp(23)
				.maxTemp(32)
				.precipitation(40)
				.status("Cloudy");
		
		DailyWeather dailyForecast2 = new DailyWeather()
				.location(location)
				.dayOfMonth(17)
				.month(7)
				.minTemp(25)
				.maxTemp(34)
				.precipitation(30)
				.status("Sunny");		
		
		location.setListDailyWeather(List.of(dailyForecast1, dailyForecast2));
		
		HourlyWeather hourlyForecast1 = new HourlyWeather()
				.location(location)
				.hourOfDay(10)
				.temperature(13)
				.precipitation(70)
				.status("Cloudy");		
		
		HourlyWeather hourlyForecast2 = new HourlyWeather()
				.location(location)
				.hourOfDay(11)
				.temperature(15)
				.precipitation(60)
				.status("Sunny");		
		
		location.setListHourlyWeathers(List.of(hourlyForecast1, hourlyForecast2));
		
		Mockito.when(locationService.getLocation(Mockito.anyString())).thenReturn(location);
		when(weatherService.getByLocation(location)).thenReturn(location);
		
		String expectedLocation = location.toString();
		
		mockMvc.perform(get(END_POINT_PATH))
				.andExpect(status().isOk())
				.andExpect((ResultMatcher) content().contentType("application/json"))
				.andExpect(jsonPath("$.location", is(expectedLocation)))
				.andExpect(jsonPath("$.realtime_weather.temperature", is(12)))
				.andExpect(jsonPath("$.hourly_forecast[0].hour_of_day", is(10)))
				.andExpect(jsonPath("$.daily_forecast[0].precipitation", is(40)))
				.andDo(print());
	}		
	
	@Test
	public void testGetByCodeShouldReturn404NotFound() throws Exception {
		String locationCode = "ABC123";
		String requestURI = END_POINT_PATH + "/" + locationCode;
		
		LocationNotFoundException ex = new LocationNotFoundException(locationCode);
		when(weatherService.get(locationCode)).thenThrow(ex);
		
		mockMvc.perform(get(requestURI))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errors[0]", is(ex.getMessage())))
				.andDo(print());		
	}
	
	@Test
	public void testGetByCodeShouldReturn200OK() throws Exception {
		String locationCode = "NYC_USA";
		String requestURI = END_POINT_PATH + "/" + locationCode;
		
		Location location = new Location();
		location.setCode(locationCode);
		location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryCode("US");
		location.setCountryName("United States of America");
		
		RealtimeWeather realtimeWeather = new RealtimeWeather();
		realtimeWeather.setTemperature(12);
		realtimeWeather.setHumidity(32);
		realtimeWeather.setLastUpdated(new Date());
		realtimeWeather.setPrecipitation(88);
		realtimeWeather.setStatus("Cloudy");
		realtimeWeather.setWindSpeed(5);
		
		location.setRealtimeWeather(realtimeWeather);
		
		DailyWeather dailyForecast1 = new DailyWeather()
				.location(location)
				.dayOfMonth(16)
				.month(7)
				.minTemp(23)
				.maxTemp(32)
				.precipitation(40)
				.status("Cloudy");
		
		DailyWeather dailyForecast2 = new DailyWeather()
				.location(location)
				.dayOfMonth(17)
				.month(7)
				.minTemp(25)
				.maxTemp(34)
				.precipitation(30)
				.status("Sunny");		
		
		location.setListDailyWeather(List.of(dailyForecast1, dailyForecast2));
		
		HourlyWeather hourlyForecast1 = new HourlyWeather()
				.location(location)
				.hourOfDay(10)
				.temperature(13)
				.precipitation(70)
				.status("Cloudy");		
		
		HourlyWeather hourlyForecast2 = new HourlyWeather()
				.location(location)
				.hourOfDay(11)
				.temperature(15)
				.precipitation(60)
				.status("Sunny");		
		
		location.setListHourlyWeathers(List.of(hourlyForecast1, hourlyForecast2));
		
		when(weatherService.get(locationCode)).thenReturn(location);
		
		String expectedLocation = location.toString();
		
		mockMvc.perform(get(requestURI))
				.andExpect(status().isOk())
				.andExpect((ResultMatcher) content().contentType("application/json"))
				.andExpect(jsonPath("$.location", is(expectedLocation)))
				.andExpect(jsonPath("$.realtime_weather.temperature", is(12)))
				.andExpect(jsonPath("$.hourly_forecast[0].hour_of_day", is(10)))
				.andExpect(jsonPath("$.daily_forecast[0].precipitation", is(40)))
				.andDo(print());
	}	
	
	@Test
	public void testUpdateShouldReturn400BadRequestBecauseNoHourlyWeather() throws Exception {
		String locationCode = "NYC_USA";
		String requestURI = END_POINT_PATH + "/" + locationCode;
		
		FullWeatherDTO fullWeatherDTO = new FullWeatherDTO();
		
		String requestBody = objectMapper.writeValueAsString(fullWeatherDTO);
		
		mockMvc.perform(put(requestURI).contentType("application/json").content(requestBody))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0]", is("Hourly weather data cannot be empty")))
				.andDo(print());
	}	
	
	@Test
	public void testUpdateShouldReturn400BadRequestBecauseNoDailyWeather() throws Exception {
		String locationCode = "NYC_USA";
		String requestURI = END_POINT_PATH + "/" + locationCode;
		
		FullWeatherDTO fullWeatherDTO = new FullWeatherDTO();
		
		HourlyWeatherDTO hourlyForecast1 = new HourlyWeatherDTO()
				.hourOfDay(10)
				.temperature(13)
				.precipitation(70)
				.status("Cloudy");			
		
		fullWeatherDTO.getListHourlyWeather().add(hourlyForecast1);
		
		String requestBody = objectMapper.writeValueAsString(fullWeatherDTO);
		
		mockMvc.perform(put(requestURI).contentType("application/json").content(requestBody))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0]", is("Daily weather data cannot be empty")))
				.andDo(print());
	}	
	
	@Test
	public void testUpdateShouldReturn400BadRequestBecauseInvalidRealtimeWeatherData() throws Exception {
		String locationCode = "NYC_USA";
		String requestURI = END_POINT_PATH + "/" + locationCode;
		
		FullWeatherDTO fullWeatherDTO = new FullWeatherDTO();
		
		HourlyWeatherDTO hourlyForecast1 = new HourlyWeatherDTO()
				.hourOfDay(10)
				.temperature(13)
				.precipitation(70)
				.status("Cloudy");			
		
		fullWeatherDTO.getListHourlyWeather().add(hourlyForecast1);
		
		DailyWeatherDTO dailyForecast1 = new DailyWeatherDTO()
				.dayOfMonth(17)
				.month(7)
				.minTemp(25)
				.maxTemp(34)
				.precipitation(30)
				.status("Sunny");		
		
		fullWeatherDTO.getListDailyWeather().add(dailyForecast1);
		
		RealtimeWeatherDTO realtimeDTO = new RealtimeWeatherDTO();
		realtimeDTO.setTemperature(122);
		realtimeDTO.setHumidity(32);
		realtimeDTO.setLastUpdated(new Date());
		realtimeDTO.setPrecipitation(88);
		realtimeDTO.setStatus("Cloudy");
		realtimeDTO.setWindSpeed(5);		
		
		fullWeatherDTO.setRealtimeWeather(realtimeDTO);
		
		String requestBody = objectMapper.writeValueAsString(fullWeatherDTO);
		
		mockMvc.perform(put(requestURI).contentType("application/json").content(requestBody))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0]", containsString("Temperature must be in the range")))
				.andDo(print());
	}		
	
	@Test
	public void testUpdateShouldReturn400BadRequestBecauseInvalidHourlyWeatherData() throws Exception {
		String locationCode = "NYC_USA";
		String requestURI = END_POINT_PATH + "/" + locationCode;
		
		FullWeatherDTO fullWeatherDTO = new FullWeatherDTO();
		
		HourlyWeatherDTO hourlyForecast1 = new HourlyWeatherDTO()
				.hourOfDay(100)
				.temperature(33)
				.precipitation(70)
				.status("Cloudy");			
		
		fullWeatherDTO.getListHourlyWeather().add(hourlyForecast1);
		
		DailyWeatherDTO dailyForecast1 = new DailyWeatherDTO()
				.dayOfMonth(17)
				.month(7)
				.minTemp(25)
				.maxTemp(34)
				.precipitation(30)
				.status("Sunny");		
		
		fullWeatherDTO.getListDailyWeather().add(dailyForecast1);
		
		RealtimeWeatherDTO realtimeDTO = new RealtimeWeatherDTO();
		realtimeDTO.setTemperature(22);
		realtimeDTO.setHumidity(32);
		realtimeDTO.setLastUpdated(new Date());
		realtimeDTO.setPrecipitation(88);
		realtimeDTO.setStatus("Cloudy");
		realtimeDTO.setWindSpeed(5);		
		
		fullWeatherDTO.setRealtimeWeather(realtimeDTO);
		
		String requestBody = objectMapper.writeValueAsString(fullWeatherDTO);
		
		mockMvc.perform(put(requestURI).contentType("application/json").content(requestBody))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0]", containsString("Hour of day must be in between")))
				.andDo(print());
	}		
	
	@Test
	public void testUpdateShouldReturn400BadRequestBecauseInvalidDailyWeatherData() throws Exception {
		String locationCode = "NYC_USA";
		String requestURI = END_POINT_PATH + "/" + locationCode;
		
		FullWeatherDTO fullWeatherDTO = new FullWeatherDTO();
		
		HourlyWeatherDTO hourlyForecast1 = new HourlyWeatherDTO()
				.hourOfDay(10)
				.temperature(33)
				.precipitation(70)
				.status("Cloudy");			
		
		fullWeatherDTO.getListHourlyWeather().add(hourlyForecast1);
		
		DailyWeatherDTO dailyForecast1 = new DailyWeatherDTO()
				.dayOfMonth(17)
				.month(7)
				.minTemp(25)
				.maxTemp(34)
				.precipitation(30)
				.status("");		
		
		fullWeatherDTO.getListDailyWeather().add(dailyForecast1);
		
		RealtimeWeatherDTO realtimeDTO = new RealtimeWeatherDTO();
		realtimeDTO.setTemperature(22);
		realtimeDTO.setHumidity(32);
		realtimeDTO.setLastUpdated(new Date());
		realtimeDTO.setPrecipitation(88);
		realtimeDTO.setStatus("Cloudy");
		realtimeDTO.setWindSpeed(5);		
		
		fullWeatherDTO.setRealtimeWeather(realtimeDTO);
		
		String requestBody = objectMapper.writeValueAsString(fullWeatherDTO);
		
		mockMvc.perform(put(requestURI).contentType("application/json").content(requestBody))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0]", containsString("Status must be in between")))
				.andDo(print());
	}	
	
	@Test
	public void testUpdateShouldReturn404NotFound() throws Exception {
		String locationCode = "NYC_USA";
		String requestURI = END_POINT_PATH + "/" + locationCode;
		
		Location location = new Location();
		location.setCode(locationCode);	
		
		FullWeatherDTO fullWeatherDTO = new FullWeatherDTO();
		
		HourlyWeatherDTO hourlyForecast1 = new HourlyWeatherDTO()
				.hourOfDay(10)
				.temperature(33)
				.precipitation(70)
				.status("Cloudy");			
		
		fullWeatherDTO.getListHourlyWeather().add(hourlyForecast1);
		
		DailyWeatherDTO dailyForecast1 = new DailyWeatherDTO()
				.dayOfMonth(17)
				.month(7)
				.minTemp(25)
				.maxTemp(34)
				.precipitation(30)
				.status("Sunny");		
		
		fullWeatherDTO.getListDailyWeather().add(dailyForecast1);
		
		RealtimeWeatherDTO realtimeDTO = new RealtimeWeatherDTO();
		realtimeDTO.setTemperature(22);
		realtimeDTO.setHumidity(32);
		realtimeDTO.setLastUpdated(new Date());
		realtimeDTO.setPrecipitation(88);
		realtimeDTO.setStatus("Cloudy");
		realtimeDTO.setWindSpeed(5);		
		
		fullWeatherDTO.setRealtimeWeather(realtimeDTO);
		
		String requestBody = objectMapper.writeValueAsString(fullWeatherDTO);		
		
		LocationNotFoundException ex = new LocationNotFoundException(locationCode);
		when(weatherService.update(Mockito.eq(locationCode), Mockito.any())).thenThrow(ex);
		
		mockMvc.perform(put(requestURI).contentType("application/json").content(requestBody))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errors[0]", is(ex.getMessage())))
				.andDo(print());
	}
	
	@Test
	public void testUpdateShouldReturn200OK() throws Exception {
		String locationCode = "NYC_USA";
		String requestURI = END_POINT_PATH + "/" + locationCode;
		
		Location location = new Location();
		location.setCode(locationCode);
		location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryCode("US");
		location.setCountryName("United States of America");	
		
		RealtimeWeather realtimeWeather = new RealtimeWeather();
		realtimeWeather.setTemperature(12);
		realtimeWeather.setHumidity(32);
		realtimeWeather.setLastUpdated(new Date());
		realtimeWeather.setPrecipitation(88);
		realtimeWeather.setStatus("Cloudy");
		realtimeWeather.setWindSpeed(5);
		
		location.setRealtimeWeather(realtimeWeather);
		
		DailyWeather dailyForecast1 = new DailyWeather()
				.location(location)
				.dayOfMonth(16)
				.month(7)
				.minTemp(23)
				.maxTemp(32)
				.precipitation(40)
				.status("Cloudy");	
		
		location.setListDailyWeather(List.of(dailyForecast1));
		
		HourlyWeather hourlyForecast1 = new HourlyWeather()
				.location(location)
				.hourOfDay(10)
				.temperature(13)
				.precipitation(70)
				.status("Cloudy");		
			
		
		location.setListHourlyWeathers(List.of(hourlyForecast1));		
		
		FullWeatherDTO fullWeatherDTO = new FullWeatherDTO();
		
		HourlyWeatherDTO hourlyForecastDTO1 = new HourlyWeatherDTO()
				.hourOfDay(10)
				.temperature(13)
				.precipitation(70)
				.status("Cloudy");			
		
		fullWeatherDTO.getListHourlyWeather().add(hourlyForecastDTO1);
		
		DailyWeatherDTO dailyForecastDTO1 = new DailyWeatherDTO()
				.dayOfMonth(16)
				.month(7)
				.minTemp(23)
				.maxTemp(32)
				.precipitation(40)
				.status("Cloudy");		
		
		fullWeatherDTO.getListDailyWeather().add(dailyForecastDTO1);
		
		RealtimeWeatherDTO realtimeDTO = new RealtimeWeatherDTO();
		realtimeDTO.setTemperature(12);
		realtimeDTO.setHumidity(32);
		realtimeDTO.setLastUpdated(new Date());
		realtimeDTO.setPrecipitation(88);
		realtimeDTO.setStatus("Cloudy");
		realtimeDTO.setWindSpeed(5);	
		
		fullWeatherDTO.setRealtimeWeather(realtimeDTO);
		
		String requestBody = objectMapper.writeValueAsString(fullWeatherDTO);
		
		when(weatherService.update(Mockito.eq(locationCode), Mockito.any())).thenReturn(location);
		
		mockMvc.perform(put(requestURI).contentType("application/json").content(requestBody))
				.andExpect(status().isOk())
				.andExpect((ResultMatcher) content().contentType("application/json"))
				.andExpect(jsonPath("$.realtime_weather.temperature", is(12)))
				.andExpect(jsonPath("$.hourly_forecast[0].hour_of_day", is(10)))
				.andExpect(jsonPath("$.daily_forecast[0].precipitation", is(40)))
				.andDo(print());
	}		
	
}
