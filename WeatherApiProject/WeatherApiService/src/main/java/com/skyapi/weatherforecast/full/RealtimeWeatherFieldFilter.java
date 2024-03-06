package com.skyapi.weatherforecast.full;

import com.skyapi.weatherforecast.realtime.RealtimeWeatherDTO;

public class RealtimeWeatherFieldFilter {
	public boolean equals(Object object) {
		if(object instanceof RealtimeWeatherDTO) {
			RealtimeWeatherDTO dto = (RealtimeWeatherDTO) object;
			return dto.getStatus() == null;		}
		return false;
	}
}
