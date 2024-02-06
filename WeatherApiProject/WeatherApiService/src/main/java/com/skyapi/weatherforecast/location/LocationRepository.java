package com.skyapi.weatherforecast.location;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.skyapi.weatherforecast.common.Location;

public interface LocationRepository extends CrudRepository<Location, String> {
	@Query("Select l from Location l where l.trashed = false")
	public List<Location> findUntrashed();
	
	@Query("Select l from Location l where l.trashed = false and l.code=?1")
	public Location findByCode(String code);
	
	@Modifying
	@Query("Update Location set trashed = true where code = ?1")
	public void trashByCode(String code);
	
	@Query("SELECT l FROM Location l WHERE l.countryCode = ?1 AND l.cityName = ?2 AND l.trashed = false")
	public Location findByCountryCodeAndCityName(String countryCode, String cityName);
}
