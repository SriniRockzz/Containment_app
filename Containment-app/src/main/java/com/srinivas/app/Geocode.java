package com.srinivas.app;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.byteowls.jopencage.JOpenCageGeocoder;
import com.byteowls.jopencage.model.JOpenCageForwardRequest;
import com.byteowls.jopencage.model.JOpenCageLatLng;
import com.byteowls.jopencage.model.JOpenCageResponse;




@RestController
public class Geocode {
	@Autowired 
	private LocationRepo locRepo;
	
	@GetMapping("/location_send") 
	public String Geo(Location loc,@RequestParam("address") String address) throws InterruptedException, ExecutionException {
		JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder("2e08f7ade19a465882a1c9c653162002");
		JOpenCageForwardRequest request = new JOpenCageForwardRequest(address);
		
		JOpenCageResponse response = jOpenCageGeocoder.forward(request);
	
		 if (response != null && response.getResults() != null && !response.getResults().isEmpty()) {
			    JOpenCageLatLng coordinates = response.getResults().get(0).getGeometry();
			    loc.setLatitude(coordinates.getLat());
			    loc.setLongitude( coordinates.getLng());
			    locRepo.save(loc);
				  
			    System.out.println(coordinates.getLat().toString() + "," + coordinates.getLng().toString());
			  } else {
			    System.out.println("Unable to geocode input address: " + address);
			  }


    
	   
	    
	 

	
	  return "success";
	}
	
	}


