package com.skyapi.weatherforecast.location;

import java.util.Date;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class LocationRepositoryTests {
	@Autowired
	private LocationRepository repository;

	@Test
	public void testAddSuccess() {
		Location location = new Location();
		location.setCode("MBMH_IN");
		location.setCityName("Mumbai");
		location.setRegionName("Maharashtra");
		location.setCountryCode("IN");
		location.setCountryName("India");
		location.setEnabled(true);

		Location savedLocation = repository.save(location);

		assertThat(savedLocation).isNotNull();
		assertThat(savedLocation.getCode()).isEqualTo("MBMH_IN");
	}

	@Test
	public void testListSuccess() {
		List<Location> locations = repository.findUntrashed();
		assertThat(locations).isNotEmpty();
		locations.forEach(System.out::println);
	}

	@Test
	public void testGetNotFound() {
		String code = "ABCD";
		Location location = repository.findByCode(code);
		assertThat(location).isNull();
	}

	@Test
	public void testGetFound() {
		String code = "LACA_USA";
		Location location = repository.findByCode(code);

		assertThat(location).isNotNull();
		assertThat(location.getCode()).isEqualTo(code);
	}

	@Test
	public void testTrashSuccess() {
		String code = "LACA_USA";
		repository.trashByCode(code);
		Location location = repository.findByCode(code);
		assertThat(location).isNull();
	}

	@Test
	public void testAddRealtimeWeatherData() {
		String code = "NYC_USA";

		Location location = repository.findByCode(code);
		RealtimeWeather realtimeWeather = location.getRealtimeWeather();
		if (realtimeWeather == null) {
			realtimeWeather = new RealtimeWeather();
			realtimeWeather.setLocation(location);
			location.setRealtimeWeather(realtimeWeather);
		}

		realtimeWeather.setTemperature(-1);
		realtimeWeather.setHumidity(30);
		realtimeWeather.setPrecipitation(70);
		realtimeWeather.setStatus("Snowy");
		realtimeWeather.setWindSpeed(15);
		realtimeWeather.setLastUpdated(new Date());

		Location updatedLocation = repository.save(location);
		assertThat(updatedLocation.getRealtimeWeather().getLocationCode()).isEqualTo(code);
	}

	@Test
	public void testAddHourlyWeatherData() {
		Location location = repository.findById("DELHI_IN").get();
		List<HourlyWeather> listHourlyWeathers = location.getListHourlyWeathers();
		HourlyWeather forecast1 = new HourlyWeather().id(location, 10).temperature(15).precipitation(40)
				.status("Sunny");
		HourlyWeather forecast2 = new HourlyWeather().location(location).hourOfDay(11).temperature(16).precipitation(50)
				.status("Cloudy");
		listHourlyWeathers.add(forecast1);
		listHourlyWeathers.add(forecast2);
		Location updatedLocation = repository.save(location);
		assertThat(updatedLocation.getListHourlyWeathers()).isNotEmpty();
	}
	
	@Test
	public void testFindByCountryCodeandCityNotFound() {
		String countryCode = "US";
		String cityName = "New York City";
		Location location = repository.findByCountryCodeAndCityName(countryCode, cityName);
		assertThat(location).isNull();
	}
	@Test
	public void testFindByCountryCodeandCityFound() {
		String countryCode = "IN";
		String cityName = "Delhi";
		Location location = repository.findByCountryCodeAndCityName(countryCode, cityName);
		assertThat(location).isNotNull();
		assertThat(location.getCountryCode()).isEqualTo(countryCode);
		assertThat(location.getCityName()).isEqualTo(cityName);
	}
	
	@Test
	public void testAddDailyWeatherData() {
		Location location = repository.findById("DELHI_IN").get();
		List<DailyWeather> listDailyWeathers=location.getListDailyWeather();
		DailyWeather forecast1 = new DailyWeather()
				.location(location)
				.dayOfMonth(16)
				.month(7)
				.minTemp(25)
				.maxTemp(33)
				.precipitation(20)
				.status("Sunny");
		
		DailyWeather forecast2 = new DailyWeather()
				.location(location)
				.dayOfMonth(17)
				.month(7)
				.minTemp(26)
				.maxTemp(34)
				.precipitation(10)
				.status("Clear");
		listDailyWeathers.add(forecast1);
		listDailyWeathers.add(forecast2);
		Location updatedLocation = repository.save(location);
		assertThat(updatedLocation.getListDailyWeather()).isNotEmpty();
	}
}
