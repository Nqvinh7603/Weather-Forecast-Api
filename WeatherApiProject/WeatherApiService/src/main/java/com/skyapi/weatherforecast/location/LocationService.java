package com.skyapi.weatherforecast.location;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skyapi.weatherforecast.common.Location;

@Service
@Transactional
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
	public Location get(String code) {
		Location location = repo.findByCode(code);
		if(location == null) {
			throw new LocationNotFoundException(code);
		}
		return location;
	}
	public Location update(Location locationInRequest){
		String code = locationInRequest.getCode();
		Location locationInDB = repo.findByCode(code);
		if(locationInDB == null) {
			throw new LocationNotFoundException(code);
		}
		locationInDB.setCityName(locationInRequest.getCityName());
		locationInDB.setRegionName(locationInRequest.getRegionName());
		locationInDB.setCountryCode(locationInRequest.getCountryCode());
		locationInDB.setCountryName(locationInRequest.getCountryName());
		locationInDB.setEnabled(locationInRequest.isEnabled());
		return repo.save(locationInDB);
	}
	public void delete(String code){
		Location location = repo.findByCode(code);
		if(location == null) {
			throw new LocationNotFoundException(code);	
		}
		repo.trashByCode(code);
	}
}
