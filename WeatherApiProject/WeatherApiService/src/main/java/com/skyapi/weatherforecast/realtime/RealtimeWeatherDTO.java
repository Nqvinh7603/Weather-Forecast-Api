package com.skyapi.weatherforecast.realtime;

import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skyapi.weatherforecast.common.Location;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;


public class RealtimeWeatherDTO {
	
	private String location;
	
	@Range(min = -50, max = 50, message = "Temperature must be in the range of -50 to 50 Celsius degree")
	private int temperature;
	
	@Range(min = 0, max = 100, message = "Humidity must be in the range of 0 to 100 percentage")
	private int humidity;
	
	@Range(min = 0, max = 100, message = "Precipitation must be in the range of 0 to 100 percentage")
	private int precipitation;
	
	@Range(min = 0, max = 200, message = "Wind speed must be in the range of 0 to 200 km/h")
	private int windSpeed;
	
	@NotBlank(message = "Status must not be empty")
	@Length(min = 3, max = 50, message = "Status must be in between 3-50 characters")	
	private String status;
		
	@JsonProperty("last_updated")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	private Date lastUpdated;
	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	public int getPrecipitation() {
		return precipitation;
	}

	public void setPrecipitation(int precipitation) {
		this.precipitation = precipitation;
	}

	public int getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(int windSpeed) {
		this.windSpeed = windSpeed;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
