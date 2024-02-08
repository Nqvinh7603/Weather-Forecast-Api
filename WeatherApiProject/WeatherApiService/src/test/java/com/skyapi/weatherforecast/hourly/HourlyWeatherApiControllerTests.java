package com.skyapi.weatherforecast.hourly;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
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

import com.skyapi.weatherforecast.GeolocationException;
import com.skyapi.weatherforecast.GeolocationService;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;

@WebMvcTest(HourlyWeatherApiController.class)
public class HourlyWeatherApiControllerTests {
	private static final String X_CURRENT_HOUR = "X-Current-Hour";
	private static final String END_POINT_PATH = "/v1/hourly";
	@Autowired
	MockMvc mockMvc;
	@MockBean
	private HourlyWeatherService hourlyWeatherService;
	@MockBean
	private GeolocationService geolocationService;

	@Test
	public void testGetByIPShouldReturn400BadRequestBecauseNoHeaderXCurrentHour() throws Exception {
		mockMvc.perform(get(END_POINT_PATH)).andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testGetByIPShouldReturn400BadRequestBecauseGeolocationException() throws Exception {
		Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenThrow(GeolocationException.class);
		mockMvc.perform(get(END_POINT_PATH).header(X_CURRENT_HOUR, "9")).andExpect(status().isBadRequest())
				.andDo(print());
	}

	@Test
	public void testGetByIPShouldReturn204NoContent() throws Exception {
		int currentHour = 9;
		Location location = new Location().code("DELHI_IN");
		Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(location);
		when(hourlyWeatherService.getByLocation(location, currentHour)).thenReturn(new ArrayList<>());
		mockMvc.perform(get(END_POINT_PATH).header(X_CURRENT_HOUR, String.valueOf(currentHour)))
				.andExpect(status().isNoContent()).andDo(print());
	}
	
	@Test
	public void testGetByIPShouldReturn200OK() throws Exception {
		int currentHour = 9;
		Location location = new Location();
		location.setCode("NYC_USA");
		location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryCode("US");
		location.setCountryName("United States of America");

		
		HourlyWeather forecast1 = new HourlyWeather().location(location).hourOfDay(10).temperature(13)
				.precipitation(70).status("Cloudy");
		
		HourlyWeather forecast2 = new HourlyWeather().location(location).hourOfDay(11).temperature(15)
				.precipitation(60).status("Sunny");

		Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(location);
		when(hourlyWeatherService.getByLocation(location, currentHour)).thenReturn(List.of(forecast1, forecast2));
		String expectedLocation = location.toString();
		mockMvc.perform(get(END_POINT_PATH).header(X_CURRENT_HOUR, String.valueOf(currentHour)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.location", is(expectedLocation)))
				.andExpect(jsonPath("$.hourly_forecast[0].hour_of_day", is(10)))
				.andDo(print());
	}
}
