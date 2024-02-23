package com.skyapi.weatherforecast.common;

import java.util.Objects;


import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="weather_hourly")
public class HourlyWeather {
	@EmbeddedId
	private HourlyWeatherId id = new HourlyWeatherId();
	private int temperature;
	
	private int precipitation;
	
	@Column(length = 50)
	private String status;
	public HourlyWeatherId getId() {
		return id;
	}
	public void setId(HourlyWeatherId id) {
		this.id = id;
	}
	public int getTemperature() {
		return temperature;
	}
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	public int getPrecipitation() {
		return precipitation;
	}
	public void setPrecipitation(int precipitation) {
		this.precipitation = precipitation;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public HourlyWeather temperature(int temp) {
		setTemperature(temp);
		return this;
	}
	public HourlyWeather precipitation(int precipitation) {
		setPrecipitation(precipitation);
		return this;
	}
	public HourlyWeather status(String status) {
		setStatus(status);
		return this;
	}
	public HourlyWeather location(Location location) {
		this.id.setLocation(location);
		return this;
	}
	public HourlyWeather id(Location location, int hour) {
		this.id.setHourOfDay(hour);
		this.id.setLocation(location);
		return this;
	}
	
	public HourlyWeather hourOfDay(int hour) {
		this.id.setHourOfDay(hour);
		return this;
	}
	@Override
	public String toString() {
		return "HourlyWeather [hourOfDay=" + id.getHourOfDay() + ", temperature=" + temperature + ", precipitation=" + precipitation
				+ ", status=" + status + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HourlyWeather other = (HourlyWeather) obj;
		return Objects.equals(id, other.id);
	}
	public HourlyWeather getShallowCopy() {
		HourlyWeather copy = new HourlyWeather();
		copy.setId(this.getId());
		return copy;
	}
	
}
