package com.skyapi.weatherforecast.location;

import java.util.List;

import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.common.Location;

@Service
public class LocationService {
	private LocationRepository repo;

	public LocationService(LocationRepository repo) {
		super();
		this.repo = repo;
	}
	public Location add (Location location) {
		return repo.save(location);
	}
	public List<Location> list(){
		return repo.findUntrashed();
	}
}
