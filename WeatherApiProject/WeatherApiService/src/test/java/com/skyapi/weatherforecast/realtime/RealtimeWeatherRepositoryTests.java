package com.skyapi.weatherforecast.realtime;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.skyapi.weatherforecast.common.RealtimeWeather;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RealtimeWeatherRepositoryTests {
	@Autowired
	private RealtimeWeatherRepository repo;
	@Test
	public void testUpdate() {
		String locationCode = "NYC_USA";
		RealtimeWeather realtimeWeather = repo.findById(locationCode).get();
		realtimeWeather.setTemperature(-2);
		realtimeWeather.setHumidity(30);
		realtimeWeather.setPrecipitation(70);
		realtimeWeather.setStatus("Snowy");
		realtimeWeather.setWindSpeed(15);
		realtimeWeather.setLastUpdated(new Date());
		RealtimeWeather updatedRealtimeWeather =repo.save(realtimeWeather);
		assertThat(updatedRealtimeWeather.getHumidity()).isEqualTo(30);
	}
}
