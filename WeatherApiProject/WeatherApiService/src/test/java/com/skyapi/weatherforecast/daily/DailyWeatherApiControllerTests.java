package com.skyapi.weatherforecast.daily;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import com.skyapi.weatherforecast.GeolocationException;
import com.skyapi.weatherforecast.GeolocationService;
import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.location.LocationNotFoundException;

@WebMvcTest(DailyWeatherApiController.class)
public class DailyWeatherApiControllerTests {
	private static final String END_POINT_PATH = "/v1/daily";
	@Autowired private MockMvc mockMvc;
	@MockBean private DailyWeatherService dailyWeatherService;
	@MockBean private GeolocationService locationService;
	
	@Test
	public void testGetByIPShouldReturn400BadRequestBecauseGeolocationException() throws Exception{
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
		when(dailyWeatherService.getByLocation(location)).thenThrow(ex);
		mockMvc.perform(get(END_POINT_PATH))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$.errors[0]", is(ex.getMessage())))
		.andDo(print());
	}
	
	@Test
	public void testGetByIPShouldReturn204NoContent() throws Exception {
		Location location = new Location().code("DELHI_IN");
		Mockito.when(locationService.getLocation(Mockito.anyString())).thenReturn(location);
		when(dailyWeatherService.getByLocation(location)).thenReturn(new ArrayList<>());
		
		LocationNotFoundException ex = new LocationNotFoundException(location.getCode());
		when(dailyWeatherService.getByLocation(location)).thenThrow(ex);
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
		
		DailyWeather forecast1 = new DailyWeather()
				.location(location)
				.dayOfMonth(16)
				.month(7)
				.minTemp(23)
				.maxTemp(32)
				.precipitation(40)
				.status("Cloudy");
		
		DailyWeather forecast2 = new DailyWeather()
				.location(location)
				.dayOfMonth(17)
				.month(7)
				.minTemp(25)
				.maxTemp(34)
				.precipitation(30)
				.status("Sunny");		
		
		Mockito.when(locationService.getLocation(Mockito.anyString())).thenReturn(location);
		when(dailyWeatherService.getByLocation(location)).thenReturn(List.of(forecast1, forecast2));
		
		String expectedLocation = location.toString();
		
		mockMvc.perform(get(END_POINT_PATH))
				.andExpect(status().isOk())
				.andExpect((ResultMatcher) content().contentType("application/json"))
				.andExpect(jsonPath("$.location", is(expectedLocation)))
				.andExpect(jsonPath("$.daily_forecast[0].day_of_month", is(16)))				
				.andDo(print());
	}		
}
