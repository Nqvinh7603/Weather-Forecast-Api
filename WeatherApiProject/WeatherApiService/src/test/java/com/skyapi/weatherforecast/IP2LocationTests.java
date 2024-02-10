package com.skyapi.weatherforecast;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import com.ip2location.IP2Location;
import com.ip2location.IPResult;
public class IP2LocationTests {
	private String DBPath = "ip2locdb/IP2LOCATION-LITE-DB3.BIN";
	
	@Test
	public void testInvalidIP() throws IOException {
		IP2Location ipLocator = new IP2Location();
		ipLocator.Open(DBPath);
		
		String ipAdress = "abc";
		IPResult ipResult = ipLocator.IPQuery(ipAdress);
		assertThat(ipResult.getStatus()).isEqualTo("INVALID_IP_ADDRESS");
		System.out.println(ipResult);
	}
	@Test
	public void testValidIP1() throws IOException {
		IP2Location ipLocator = new IP2Location();
		ipLocator.Open(DBPath);
		
		String ipAdress = "101.96.64.0";// Ha Noi
		IPResult ipResult = ipLocator.IPQuery(ipAdress);
		assertThat(ipResult.getStatus()).isEqualTo("OK");
		assertThat(ipResult.getCity()).isEqualTo("Hanoi");
		System.out.println(ipResult);
	}
	@Test
	public void testValidIP2() throws IOException {
		IP2Location ipLocator = new IP2Location();
		ipLocator.Open(DBPath);
		
		String ipAdress = "103.48.198.141";// Delhi
		IPResult ipResult = ipLocator.IPQuery(ipAdress);
		assertThat(ipResult.getStatus()).isEqualTo("OK");
		assertThat(ipResult.getCity()).isEqualTo("Delhi");
		System.out.println(ipResult);
	}
}
