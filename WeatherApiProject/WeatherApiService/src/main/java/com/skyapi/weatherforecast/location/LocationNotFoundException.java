package com.skyapi.weatherforecast.location;

public class LocationNotFoundException extends RuntimeException {

	public LocationNotFoundException(String locationCode) {
		super("No location found with the given code: " + locationCode);
	}
	public LocationNotFoundException(String countryCode, String cityName) {
		super("No location found with the given code: " + countryCode + " and city name: " + cityName);
	}

}
