package com.cvb.javaapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class AppController {
  
  private String REQUEST_URI = "http://bpdts-test-app-v4.herokuapp.com";
	
	public String index(RestTemplate restTemplate) throws Exception {
		Users users[] = restTemplate.getForObject("http://bpdts-test-app-v4.herokuapp.com/users", Users[].class);
		return users.toString();
	}

	@RequestMapping("/")
	public List<Users> getAll(RestTemplate restTemplate) throws Exception {
		ResponseEntity<Users[]> entity = restTemplate.getForEntity(REQUEST_URI + "/users", Users[].class);
		return entity.getBody() != null ? Arrays.asList(entity.getBody()) : Collections.emptyList();
  }
  
  @RequestMapping("/london")
  public List<Users> getLondonUser(RestTemplate restTemplate) throws Exception {
    String requestUri = REQUEST_URI + "/city/London/users";

    ResponseEntity<Users[]> entity = restTemplate.getForEntity(requestUri, Users[].class);
		return entity.getBody() != null ? Arrays.asList(entity.getBody()) : Collections.emptyList();
  }

  @RequestMapping("/within-london-radius")
  public List<Users> getRadiusLondonUsers(RestTemplate restTemplate) throws Exception {
    float radius_from_london = Float.parseFloat("96.5606"); //kilometers, 60 miles

    float lat_degree_60_miles = radius_from_london / 111; // 111km aprox 1 latitude degree 
    float london_latitude = Float.parseFloat("51.507247");
    double london_latitude_d = london_latitude;
    float max_latitude_north = london_latitude + lat_degree_60_miles;
    float max_latitude_south = london_latitude - lat_degree_60_miles;

    float london_longitude = Float.parseFloat("-0.127716");

    float r_earth = Float.parseFloat("6371"); //kilometers
    double long_degree_km = ( (2*Math.PI)/360 ) * r_earth * Math.cos(london_latitude_d); //per longitude degree
    float long_degree_60_miles = radius_from_london / (float)long_degree_km;
    float max_longitude_east = london_longitude + long_degree_60_miles;
    float max_longitude_west = london_longitude + ( long_degree_60_miles * -1 );

    ResponseEntity<Users[]> entity = restTemplate.getForEntity(REQUEST_URI + "/users", Users[].class);
    
    List<Users> listUsers = Arrays.asList(entity.getBody());
    List<Users> listWithinLondon = new ArrayList<Users>();
    for(Users element : listUsers){
      if((max_latitude_south < element.getLatitude() && element.getLatitude() < max_latitude_north) &&
         (max_longitude_west < element.getLongitude() && element.getLongitude() < max_longitude_east))
        listWithinLondon.add(element);
      
    }

    return listWithinLondon;
  }

}