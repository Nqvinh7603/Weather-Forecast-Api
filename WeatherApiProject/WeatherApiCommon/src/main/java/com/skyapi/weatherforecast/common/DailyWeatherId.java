package com.skyapi.weatherforecast.common;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class DailyWeatherId implements Serializable {

	private int dayOfMonth;
	private int month;
	
	@ManyToOne
	@JoinColumn(name = "location_code")
	private Location location;

	public DailyWeatherId() { }
	
	public DailyWeatherId(int dayOfMonth, int month, Location location) {
		super();
		this.dayOfMonth = dayOfMonth;
		this.month = month;
		this.location = location;
	}

	public int getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "DailyWeatherId [dayOfMonth=" + dayOfMonth + ", month=" + month + ", location=" + location + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(dayOfMonth, location, month);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DailyWeatherId other = (DailyWeatherId) obj;
		return dayOfMonth == other.dayOfMonth && Objects.equals(location, other.location) && month == other.month;
	}
	
	
}
