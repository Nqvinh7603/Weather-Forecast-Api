package com.skyapi.weatherforecast.common;


import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "weather_daily")
public class DailyWeather {
	@EmbeddedId
	private DailyWeatherId id = new DailyWeatherId();
	
	private int minTemp;
	private int maxTemp;
	private int precipitation;
	
	@Column(length = 50)
	private String status;

	public DailyWeatherId getId() {
		return id;
	}

	public void setId(DailyWeatherId id) {
		this.id = id;
	}

	public int getMinTemp() {
		return minTemp;
	}

	public void setMinTemp(int minTemp) {
		this.minTemp = minTemp;
	}

	public int getMaxTemp() {
		return maxTemp;
	}

	public void setMaxTemp(int maxTemp) {
		this.maxTemp = maxTemp;
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
	public DailyWeather precipitation(int precipitation) {
		setPrecipitation(precipitation);
		return this;
	}
	public DailyWeather status(String status) {
		setStatus(status);
		return this;
	}
	public DailyWeather location(Location location) {
		this.id.setLocation(location);
		return this;
	}
	public DailyWeather dayOfMonth(int month) {
		this.id.setDayOfMonth(month);
		return this;
	}
	public DailyWeather month(int month) {
		this.id.setMonth(month);
		return this;
	}
	public DailyWeather minTemp(int temp) {
		setMinTemp(temp);
		return this;
	}
	public DailyWeather maxTemp(int temp) {
		setMaxTemp(temp);
		return this;
	}
	public DailyWeather getShallowCopy() {
		DailyWeather copy = new DailyWeather();
		copy.setId(this.getId());
		
		return copy;
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
		DailyWeather other = (DailyWeather) obj;
		return Objects.equals(id, other.id);
	}	
	
	@Override
	public String toString() {
		return "DailyWeather [id=" + id + ", minTemp=" + minTemp + ", maxTemp=" + maxTemp + ", precipitation="
				+ precipitation + ", status=" + status + "]";
	}

	
}
