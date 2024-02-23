package com.skyapi.weatherforecast.common;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class HourlyWeatherId implements Serializable {
	private int hourOfDay;
	@ManyToOne
	@JoinColumn(name="location_code")
	private Location location;
	public int getHourOfDay() {
		return hourOfDay;
	}
	public void setHourOfDay(int hourOfDay) {
		this.hourOfDay = hourOfDay;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public HourlyWeatherId(int hourOfDay, Location location) {
		super();
		this.hourOfDay = hourOfDay;
		this.location = location;
	}
	public HourlyWeatherId() {
		super();
	}
	@Override
	public int hashCode() {
		return Objects.hash(hourOfDay, location);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HourlyWeatherId other = (HourlyWeatherId) obj;
		return hourOfDay == other.hourOfDay && Objects.equals(location, other.location);
	}
	
	
}
